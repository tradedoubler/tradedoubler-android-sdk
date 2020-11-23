package com.tradedouble.tradedoublerandroid

import okhttp3.internal.and
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

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

    private fun generateMD5(text: String): String? {
        val md = MessageDigest.getInstance("MD5")
        val bigInt = BigInteger(1, md.digest(text.toByteArray(Charsets.UTF_8)))
        return String.format("%032x", bigInt)
    }

    fun generateCheckSum(secretCode: String, orderNumber: String, orderValue: String): String {
        val prefix = "v04"
        val suffix = secretCode + orderNumber + orderValue
        return prefix + generateMD5(suffix)
    }

}
