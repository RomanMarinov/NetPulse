package app.romanmarinov.netpulse

data class ConnectivityType(
    val wifi: Boolean,
    val cellular: Boolean,
    val vpn: Boolean,
)