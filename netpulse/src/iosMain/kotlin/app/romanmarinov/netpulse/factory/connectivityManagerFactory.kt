package app.romanmarinov.netpulse.factory

import app.romanmarinov.netpulse.ConnectivityTypeManager
import app.romanmarinov.netpulse.ConnectivityTypeManagerImpl

fun connectivityManagerFactory(): ConnectivityTypeManager =
    ConnectivityTypeManagerImpl()