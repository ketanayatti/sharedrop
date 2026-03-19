package org.nitish.project.sharedrop

import java.io.File
import java.util.Properties

private const val localNameKey = "local_name"

actual fun provideDeviceNameStorage(): DeviceNameStorage = JvmDeviceNameStorage()

private class JvmDeviceNameStorage : DeviceNameStorage {
    private val dataDirectory = File(System.getProperty("user.home"), ".sharedrop").apply {
        mkdirs()
    }
    private val propertiesFile = File(dataDirectory, "settings.properties")

    override suspend fun getLocalName(): String? {
        if (!propertiesFile.exists()) {
            return null
        }

        return Properties().apply {
            propertiesFile.inputStream().use { input ->
                load(input)
            }
        }.getProperty(localNameKey)
    }

    override suspend fun setLocalName(name: String) {
        val properties = Properties()
        if (propertiesFile.exists()) {
            propertiesFile.inputStream().use { input ->
                properties.load(input)
            }
        }
        properties.setProperty(localNameKey, name)
        propertiesFile.outputStream().use { output ->
            properties.store(output, "ShareDrop settings")
        }
    }
}
