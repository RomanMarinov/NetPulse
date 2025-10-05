package app.romanmarinov.sample

import androidx.compose.ui.window.ComposeUIViewController
import app.romanmarinov.netpulse.factory.connectivityManagerFactory

fun MainViewController() = ComposeUIViewController {
    App(manager = connectivityManagerFactory())
}