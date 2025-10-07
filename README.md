# NetPulse - Compose Multiplatform

[![Maven Central](https://img.shields.io/maven-central/v/io.github.romanmarinov.netpulse/netpulse)](https://search.maven.org/artifact/io.github.romanmarinov.netpulse/netpulse)

## Требования к сборке

[![Kotlin 2.2.0](https://img.shields.io/badge/Kotlin-2.2.0-blue?style=flat-square)](https://kotlinlang.org/)&#32;
[![Compose Multiplatform 1.8.2](https://img.shields.io/badge/Compose%20Multiplatform-1.8.2-purple?style=flat-square)](https://github.com/JetBrains/compose-multiplatform)&#32;
[![Android Gradle Plugin 8.10.1](https://img.shields.io/badge/Android%20Gradle%20Plugin-8.10.1-green?style=flat-square)](https://developer.android.com/studio/releases/gradle-plugin)


Библиотека для определения статуса интернет-подключения (**Wi-Fi, Cellular, VPN**) в проектах на **Compose Multiplatform** для Android и iOS.  

Предоставляет единый API для проверки текущего состояния сети и отслеживания изменений в реальном времени.

## Возможности
  
- Проверка типов подключения (Wi-Fi, Cellular, VPN)  
- Получение изменений состояния сети через подписку или по запросу
- Поддержка Android и iOS
- Простая интеграция с **Compose Multiplatform**  

## Установка

Добавьте зависимость из **Maven Central**:
#### commonMain sourceSet 
```
sourceSets {
    commonMain.dependencies {
         implementation("io.github.romanmarinov.netpulse:netpulse:<version>")
    }
}
```
#### manifest 
```
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```
## 1. Создайте менеджер для отслеживания типа подключения
#### AndroidMain sourceSet 
```
import app.romanmarinov.netpulse.ConnectivityTypeManager
import app.romanmarinov.netpulse.factory.connectivityManagerFactory
import util.AppContextPlatform

actual fun connectivityManagerPlatform(): ConnectivityTypeManager {
    val context = AppContextPlatform.get() // ваш способ получения context
    return connectivityManagerFactory(context = context)
}
```
#### iosMain sourceSet 
```
import app.romanmarinov.netpulse.ConnectivityTypeManager
import app.romanmarinov.netpulse.factory.connectivityManagerFactory

actual fun connectivityManagerPlatform(): ConnectivityTypeManager {
    return connectivityManagerFactory()
}
```
## 2. Получение текущего статуса сети
#### commonMain sourceSet 
```
val connectivityTypeManager: ConnectivityTypeManager = connectivityManagerPlatform()
var stateNetworkSync by remember { mutableStateOf<ConnectivityType?>(null) }

// Получение текущего типа подключения по кнопке
Button(onClick = { stateNetworkSync = connectivityTypeManager.getType() }) {
    Text("Проверить сеть")
}

LaunchedEffect(stateNetworkSync) {
    Logger.d("VPN: ${stateNetworkSync?.vpn}")
    Logger.d("Wi-Fi: ${stateNetworkSync?.wifi}")
    Logger.d("Cellular: ${stateNetworkSync?.cellular}")
}
```
## 3. Подписка на изменения соединения
#### commonMain sourceSet 
```
var stateNetworkAsync by remember { mutableStateOf<ConnectivityType?>(null) }

LaunchedEffect(connectivityTypeManager) {
    connectivityTypeManager.observeType().collectLatest { connectivityType ->
        stateNetworkAsync = connectivityType
    }
}

LaunchedEffect(stateNetworkAsync) {
    Logger.d("VPN: ${stateNetworkAsync?.vpn}")
    Logger.d("Wi-Fi: ${stateNetworkAsync?.wifi}")
    Logger.d("Cellular: ${stateNetworkAsync?.cellular}")
}
```
4. Проверка конкретного типа подключения
// TODO: пример проверки Wi-Fi / Cellular / VPN

## iOS экспорт библиотеки
```
listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
    iosTarget.binaries.framework {
        export("app.romanmarinov.netpulse:compose-connectivity:<version>")
    }
}
```
## Минимальные требования

Android: minSdk 24

iOS: iOS 14.1+

## Настройка платформ
Android

Дополнительная настройка не требуется.

iOS

Добавьте необходимые разрешения в Info.plist:
```
<key>NSAppTransportSecurity</key>
<dict>
  <key>NSAllowsArbitraryLoads</key><true/>
</dict>
```
// TODO: пример вызова в AppDelegate / SceneDelegate

## Документация

Полное описание API: [в разработке]

## Полезные ссылки

TODO: добавить ссылки на статьи / примеры

## Лицензия

Apache 2.0
