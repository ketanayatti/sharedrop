package org.nitish.project.sharedrop

expect class FileSender() {
    fun sendFile(
        host: String,
        port: Int,
        fileName: String,
        bytes: ByteArray,
        onProgress: (Float) -> Unit,
        onResult: (Boolean) -> Unit)
}
