package app.romanmarinov.netpulse.factory

import android.content.Context
import app.romanmarinov.netpulse.ConnectivityTypeManager
import app.romanmarinov.netpulse.ConnectivityTypeManagerImpl

// Вспомогательная фабрика для Android, которая принимает Context
fun connectivityManagerFactory(context: Context): ConnectivityTypeManager =
    ConnectivityTypeManagerImpl(context)