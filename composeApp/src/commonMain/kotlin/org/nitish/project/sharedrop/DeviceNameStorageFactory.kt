package org.nitish.project.sharedrop

interface DeviceNameStorage {
    suspend fun getLocalName(): String?
    suspend fun setLocalName(name: String)
}

expect fun provideDeviceNameStorage(): DeviceNameStorage
