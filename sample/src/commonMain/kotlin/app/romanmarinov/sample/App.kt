package app.romanmarinov.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.romanmarinov.netpulse.ConnectivityType
import app.romanmarinov.netpulse.ConnectivityTypeManager
import kotlinx.coroutines.flow.collectLatest
import netpulse_main.sample.generated.resources.Res
import netpulse_main.sample.generated.resources.button_text
import netpulse_main.sample.generated.resources.cellular_async
import netpulse_main.sample.generated.resources.cellular_sync
import netpulse_main.sample.generated.resources.title
import netpulse_main.sample.generated.resources.unknown
import netpulse_main.sample.generated.resources.vpn_async
import netpulse_main.sample.generated.resources.vpn_sync
import netpulse_main.sample.generated.resources.wifi_async
import netpulse_main.sample.generated.resources.wifi_sync
import org.jetbrains.compose.resources.stringResource

@Composable
fun App(manager: ConnectivityTypeManager) {
    var stateNetworkAsync by remember { mutableStateOf<ConnectivityType?>(null) }
    var stateNetworkSync by remember { mutableStateOf<ConnectivityType?>(null) }

    LaunchedEffect(manager) {
        manager.observeType().collectLatest {
            stateNetworkAsync = it
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.title),
                style = MaterialTheme.typography.headlineMedium
            )

            Text(text = "${stringResource(Res.string.wifi_async)} ${stateNetworkAsync?.wifi}")
            Text(text = "${stringResource(Res.string.cellular_async)} ${stateNetworkAsync?.cellular}")
            Text(text = "${stringResource(Res.string.vpn_async)} ${stateNetworkAsync?.vpn}")

            Button(
                onClick = {
                    stateNetworkSync = manager.getType()
                },
                content = {
                    Text(text = stringResource(Res.string.button_text))
                }
            )

            Text(
                text = "${stringResource(Res.string.wifi_sync)} ${
                    stateNetworkSync?.wifi ?: stringResource(
                        Res.string.unknown
                    )
                }"
            )
            Text(
                text = "${stringResource(Res.string.cellular_sync)} ${
                    stateNetworkSync?.cellular ?: stringResource(
                        Res.string.unknown
                    )
                }"
            )
            Text(
                text = "${stringResource(Res.string.vpn_sync)} ${
                    stateNetworkSync?.vpn ?: stringResource(
                        Res.string.unknown
                    )
                }"
            )
        }
    }
}
