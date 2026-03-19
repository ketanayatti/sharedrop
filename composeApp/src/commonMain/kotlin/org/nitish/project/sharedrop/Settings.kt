package org.nitish.project.sharedrop

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class Settings(
    private val deviceNameStorage: DeviceNameStorage
) {
    private val _settingsFlow = MutableStateFlow(AppSettings())
    val settingsFlow: StateFlow<AppSettings> = _settingsFlow.asStateFlow()

    suspend fun ensureLoaded() {
        if (_settingsFlow.value.localName != null) {
            return
        }

        val existingName = deviceNameStorage.getLocalName()
        val localName = existingName ?: NameGenerator.generate().also { generatedName ->
            deviceNameStorage.setLocalName(generatedName)
        }

        _settingsFlow.value = AppSettings(localName = localName)
    }

    suspend fun updateLocalName(name: String) {
        deviceNameStorage.setLocalName(name)
        _settingsFlow.value = AppSettings(localName = name)
    }
}

data class AppSettings(
    val localName: String? = null
)
