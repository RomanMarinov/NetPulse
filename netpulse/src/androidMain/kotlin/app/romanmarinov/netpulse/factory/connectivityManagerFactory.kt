package app.romanmarinov.netpulse.factory

import android.content.Context
import app.romanmarinov.netpulse.ConnectivityTypeManager
import app.romanmarinov.netpulse.ConnectivityTypeManagerImpl

fun connectivityManagerFactory(context: Context): ConnectivityTypeManager =
    ConnectivityTypeManagerImpl(context)