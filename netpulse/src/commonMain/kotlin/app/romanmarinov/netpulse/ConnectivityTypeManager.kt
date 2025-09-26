package app.romanmarinov.netpulse

import kotlinx.coroutines.flow.Flow

/**
 * Унифицированный интерфейс для получения информации о подключении.
 *
 * - [getType] — единоразовое получение состояния.
 * - [observeType] — поток изменений состояния сети, чтобы удобно интегрировать в Compose.
 */
interface ConnectivityTypeManager {
    fun getType(): ConnectivityType?

    /**
     * Реактивное API.
     * Поток эмитит состояние сети при изменениях.
     */
    fun observeType(): Flow<ConnectivityType>
}