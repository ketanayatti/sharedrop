package org.nitish.project.sharedrop

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.AES
import dev.whyoleg.cryptography.algorithms.EC
import dev.whyoleg.cryptography.algorithms.ECDH

object CryptoEngine {
    class KeyPairResult(val privateKey: ECDH.PrivateKey, val publicKeyBytes: ByteArray)

    // Generates ECDH key pair (NIST P-256) for the handshake
    suspend fun generateKeyPair(): KeyPairResult {
        val keyPair = CryptographyProvider.Default.get(ECDH).keyPairGenerator(curve = EC.Curve.P256)
            .generateKey()
        return KeyPairResult(
            privateKey = keyPair.privateKey,
            publicKeyBytes = keyPair.publicKey.encodeToByteArray(format = EC.PublicKey.Format.RAW)
        )
    }

    // Derives a shared secret (AES key) using the local private key and remote public key
    suspend fun deriveAesKey(
        privateKey: ECDH.PrivateKey,
        remotePublicKeyBytes: ByteArray
    ): ByteArray {
        val remotePublicKey =
            CryptographyProvider.Default.get(ECDH).publicKeyDecoder(curve = EC.Curve.P256)
                .decodeFromByteArray(format = EC.PublicKey.Format.RAW, bytes = remotePublicKeyBytes)
        return privateKey.sharedSecretGenerator()
            .generateSharedSecretToByteArray(other = remotePublicKey)
    }

    // Encrypts payload using AES-GCM.
    // The library uses Implicit IV: automatically generates the nonce and appends the MAC tag.
    suspend fun encrypt(keyBytes: ByteArray, data: ByteArray): ByteArray {
        val key = CryptographyProvider.Default.get(AES.GCM).keyDecoder()
            .decodeFromByteArray(AES.Key.Format.RAW, keyBytes)
        return key.cipher().encrypt(data)
    }

    // Decrypts payload using AES-GCM.
    // Automatically extracts the Implicit IV from the first 12 bytes and verifies the MAC tag.
    suspend fun decrypt(keyBytes: ByteArray, encryptedPayload: ByteArray): ByteArray {
        val key = CryptographyProvider.Default.get(AES.GCM).keyDecoder()
            .decodeFromByteArray(AES.Key.Format.RAW, keyBytes)
        return key.cipher().decrypt(encryptedPayload)
    }
}