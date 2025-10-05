# NetPulse

[![Maven Central](https://img.shields.io/maven-central/v/app.romanmarinov.netpulse/compose-connectivity)](https://central.sonatype.com/artifact/app.romanmarinov.netpulse/compose-connectivity)
![badge-android]
![badge-ios]

## Требования к сборке

[![Kotlin 2.2.10](https://img.shields.io/badge/Kotlin-2.2.10-blue?style=flat-square)](https://kotlinlang.org/)  
[![Compose Multiplatform 1.8.2](https://img.shields.io/badge/Compose%20Multiplatform-1.8.2-purple?style=flat-square)](https://github.com/JetBrains/compose-multiplatform)  
[![Android Gradle Plugin 8.10.1](https://img.shields.io/badge/Android%20Gradle%20Plugin-8.10.1-green?style=flat-square)](https://developer.android.com/studio/releases/gradle-plugin)


Библиотека для определения статуса интернет-подключения (**Wi-Fi, Cellular, VPN**) в проектах на **Compose Multiplatform** для Android и iOS.  

Предоставляет единый API для проверки текущего состояния сети и отслеживания изменений в реальном времени.

## Возможности
  
- Проверка типов подключения (Wi-Fi, Cellular, VPN)  
- Получение изменений состояния сети через подписку или по запросу
- Поддержка Android и iOS
- Простая интеграция с **Compose Multiplatform**  

## Установка

Добавьте зависимость из **Maven Central** в `commonMain`:

```
kotlin
sourceSets {
    commonMain.dependencies {
        api("app.romanmarinov.netpulse:compose-connectivity:<version>")
    }
}
```
## Использование
1. Инициализация
// TODO: пример инициализации библиотеки

2. Получение текущего статуса сети
// TODO: пример получения текущего статуса подключения

3. Подписка на изменения соединения
// TODO: пример наблюдения за изменениями статуса сети

4. Проверка конкретного типа подключения
// TODO: пример проверки Wi-Fi / Cellular / VPN

## iOS экспорт библиотеки
```
kotlin
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
