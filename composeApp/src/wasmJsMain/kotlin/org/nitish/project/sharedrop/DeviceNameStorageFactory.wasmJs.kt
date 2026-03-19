package org.nitish.project.sharedrop

import kotlinx.browser.window

private const val localNameKey = "local_name"

actual fun provideDeviceNameStorage(): DeviceNameStorage = WasmDeviceNameStorage()

private class WasmDeviceNameStorage : DeviceNameStorage {
    override suspend fun getLocalName(): String? = window.localStorage.getItem(localNameKey)

    override suspend fun setLocalName(name: String) {
        window.localStorage.setItem(localNameKey, name)
    }
}
