package org.nitish.project.sharedrop

import platform.Foundation.NSUserDefaults

private const val localNameKey = "local_name"

actual fun provideDeviceNameStorage(): DeviceNameStorage = AppleDeviceNameStorage()

private class AppleDeviceNameStorage : DeviceNameStorage {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    override suspend fun getLocalName(): String? = userDefaults.stringForKey(localNameKey)

    override suspend fun setLocalName(name: String) {
        userDefaults.setObject(name, forKey = localNameKey)
    }
}
