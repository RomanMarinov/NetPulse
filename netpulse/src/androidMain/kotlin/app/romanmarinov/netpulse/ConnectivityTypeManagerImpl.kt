package app.romanmarinov.netpulse

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@SuppressLint("MissingPermission")
class ConnectivityTypeManagerImpl(
    private val context: Context
) : ConnectivityTypeManager {

    override fun getType(): ConnectivityType? = readNetworkState()

    override fun observeType(): Flow<ConnectivityType> = callbackFlow {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                readNetworkState()?.let { trySend(it) }
            }

            override fun onLost(network: Network) {
                readNetworkState()?.let { trySend(it) }
            }

            override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) {
                readNetworkState()?.let { trySend(it) }
            }
        }

        val request = NetworkRequest.Builder().build()
        cm.registerNetworkCallback(request, callback)

        readNetworkState()?.let { trySend(it) }
        awaitClose { cm.unregisterNetworkCallback(callback) }
    }

    private fun readNetworkState(): ConnectivityType? = try {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val caps = cm.getNetworkCapabilities(cm.activeNetwork)
        val wifi = caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
        val cellular = caps?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
        val vpn = caps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true
        ConnectivityType(wifi = wifi, cellular = cellular, vpn = vpn)
    } catch (_: Exception) {
        null
    }
}
