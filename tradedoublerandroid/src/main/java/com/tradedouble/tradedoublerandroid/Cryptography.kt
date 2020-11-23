package com.tradedouble.tradedoublerandroid

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object Cryptography {

    fun generateSHA56Hash(base: String?): String {

        if (base != null) {
            return try {
                val digest =
                    MessageDigest.getInstance("SHA-256")
                val hash =
                    digest.digest(base.toByteArray(StandardCharsets.UTF_8))
                val hexString = StringBuffer()
                for (i in hash.indices) {
                    val hex = Integer.toHexString(0xff and hash[i].toInt())
                    if (hex.length == 1) hexString.append('0')
                    hexString.append(hex)
                }
                hexString.toString()
            } catch (ex: Exception) {
                throw RuntimeException(ex)
            }
        }
        return ""
    }
}