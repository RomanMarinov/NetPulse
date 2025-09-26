package app.romanmarinov.netpulse

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.value
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.CFNetwork.CFNetworkCopySystemProxySettings
import platform.CoreFoundation.CFTypeRef
import platform.Foundation.CFBridgingRelease
import platform.Foundation.NSDictionary
import platform.Foundation.allKeys
import platform.Network.nw_interface_type_cellular
import platform.Network.nw_interface_type_wifi
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_t
import platform.Network.nw_path_uses_interface_type
import platform.SystemConfiguration.SCNetworkReachabilityCreateWithAddress
import platform.SystemConfiguration.SCNetworkReachabilityFlagsVar
import platform.SystemConfiguration.SCNetworkReachabilityGetFlags
import platform.SystemConfiguration.SCNetworkReachabilityRef
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsConnectionAutomatic
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsConnectionOnDemand
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsConnectionOnTraffic
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsConnectionRequired
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsInterventionRequired
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsIsDirect
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsIsLocalAddress
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsIsWWAN
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsReachable
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsTransientConnection
import platform.darwin.dispatch_get_main_queue
import platform.posix.AF_INET
import platform.posix.sockaddr_in

// Реализация ConnectivityTypeManager для iOS без сторонних библиотек
class ConnectivityTypeManagerImpl : ConnectivityTypeManager {

    private val monitor = nw_path_monitor_create()

    @OptIn(ExperimentalForeignApi::class)
     override fun getType(): ConnectivityType? {
        val reachabilityRef = createReachability() ?: return null
        val flags = getReachabilityFlags(reachabilityRef)
        val isReachable = flags.contains(kSCNetworkReachabilityFlagsReachable)
        val needsConnection = flags.contains(kSCNetworkReachabilityFlagsConnectionRequired)
        val isMobile = flags.contains(kSCNetworkReachabilityFlagsIsWWAN)

        val wifi = isReachable && !needsConnection && !isMobile
        val cellular = isMobile && isReachable && !needsConnection
        val vpn = checkVpnStatus()

        return ConnectivityType(wifi = wifi, cellular = cellular, vpn = vpn)
    }
    override fun observeType(): Flow<ConnectivityType> = callbackFlow {
        val queue = dispatch_get_main_queue()
        nw_path_monitor_set_queue(monitor, queue)

        val handler: (nw_path_t?) -> Unit = { path ->
            path?.let {
                val wifi = nw_path_uses_interface_type(it, nw_interface_type_wifi)
                val cellular = nw_path_uses_interface_type(it, nw_interface_type_cellular)
                val vpn = checkVpnStatus()

                trySend(ConnectivityType(wifi = wifi, cellular = cellular, vpn = vpn))
            }
        }

        nw_path_monitor_set_update_handler(monitor, handler)
        nw_path_monitor_start(monitor)

        awaitClose { nw_path_monitor_cancel(monitor) }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun checkVpnStatus(): Boolean = try {
        val proxySettings = CFNetworkCopySystemProxySettings() ?: return false
        val nsDict = CFBridgingRelease(proxySettings as CFTypeRef) as? NSDictionary ?: return false
        val scopedDict = nsDict.objectForKey("__SCOPED__") as? NSDictionary ?: return false
        val vpnInterfaces = listOf("utun", "ppp", "tun", "tap", "ipsec")
        scopedDict.allKeys.any { key ->
            (key as? String)?.let { iface ->
                vpnInterfaces.any { iface.startsWith(it) }
            } == true
        }
    } catch (_: Exception) {
        false
    }

    /** Создаёт SCNetworkReachabilityRef для "0.0.0.0" */
    @OptIn(ExperimentalForeignApi::class)
    private fun createReachability(): SCNetworkReachabilityRef? {
        memScoped {
            val addr = alloc<sockaddr_in>().apply {
                sin_len = sizeOf<sockaddr_in>().toUByte()
                sin_family = AF_INET.convert()
                sin_port = 0u
                sin_addr.s_addr = 0u
            }
            return SCNetworkReachabilityCreateWithAddress(null, addr.ptr.reinterpret())
        }
    }

    /** Получение флагов сети */
    @OptIn(ExperimentalForeignApi::class)
    private fun getReachabilityFlags(ref: SCNetworkReachabilityRef): Set<UInt> {
        memScoped {
            val flagsVar = alloc<SCNetworkReachabilityFlagsVar>()
            if (SCNetworkReachabilityGetFlags(ref, flagsVar.ptr)) {
                val flags = flagsVar.value
                val result = mutableSetOf<UInt>()
                // проверяем основные флаги вручную
                val allFlags = listOf(
                    kSCNetworkReachabilityFlagsTransientConnection,
                    kSCNetworkReachabilityFlagsReachable,
                    kSCNetworkReachabilityFlagsConnectionRequired,
                    kSCNetworkReachabilityFlagsConnectionOnTraffic,
                    kSCNetworkReachabilityFlagsInterventionRequired,
                    kSCNetworkReachabilityFlagsConnectionOnDemand,
                    kSCNetworkReachabilityFlagsIsLocalAddress,
                    kSCNetworkReachabilityFlagsIsDirect,
                    kSCNetworkReachabilityFlagsIsWWAN,
                    kSCNetworkReachabilityFlagsConnectionAutomatic
                )
                allFlags.forEach { flag ->
                    if ((flags and flag) != 0u) {
                        result.add(flag)
                    }
                }
                return result
            }
        }
        return emptySet()
    }
}
