package org.nitish.project.sharedrop

import java.io.ByteArrayInputStream
import java.net.Socket

actual class FileSender {
    actual fun sendFile(
        host: String,
        port: Int,
        fileName: String,
        bytes: ByteArray,
        onProgress: (Float) -> Unit,
        onResult: (Boolean) -> Unit
    ) {
        Thread {
            try {
                val socket = Socket(host, port)
                val output = socket.getOutputStream()
                val fileNameBytes = fileName.toByteArray()
                output.write(fileNameBytes.size)
                output.write(fileNameBytes)

                val totalBytes = bytes.size.toLong()
                var bytesSent = 0L
                val inputStream = ByteArrayInputStream(bytes)
                val buffer = ByteArray(8192)

                var bytesRead = inputStream.read(buffer)
                while (bytesRead != -1) {
                    output.write(buffer, 0, bytesRead)
                    bytesSent += bytesRead

                    onProgress(bytesSent.toFloat() / totalBytes.toFloat())

                    bytesRead = inputStream.read(buffer)
                }

                output.flush()
                socket.close()
                onResult(true)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }.start()
    }
}