/*
 * Copyright 2020 Tradedoubler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.tradedoubler.sdk

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import com.tradedoubler.sdk.utils.Constant.LENGTH_STRING
import com.tradedoubler.sdk.utils.Constant.TDUID
import java.math.BigInteger
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

internal object TradeDoublerSdkUtils {

    fun generateSHA56Hash(base: String?): String {

        if (base != null) {
            return try {
                val digest = MessageDigest.getInstance("SHA-256")
                val hash = digest.digest(base.toByteArray(StandardCharsets.UTF_8))
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

    fun getRandomString(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..LENGTH_STRING).map { allowedChars.random() }.joinToString("")
    }

    fun getInstallDate(context: Context): String {
        return try {
            "${context.packageManager
                .getPackageInfo(context.packageName, 0).firstInstallTime}"
        } catch (e: PackageManager.NameNotFoundException) {
            "${Calendar.getInstance().timeInMillis}"
        }
    }

    @SuppressLint("HardwareIds")
    fun getAndroidId(context: Context): String{
        return try {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: SecurityException){
            getInstallDate(context)
        }
    }

    fun extractTduidFromUri(uri: Uri?): String?{
        return uri?.let { uriNotNull ->
            uriNotNull.queryParameterNames.firstOrNull { it.equals(TDUID, true) }?.let { key ->
                uriNotNull.getQueryParameter(key)
            }
        }
    }

    fun extractTduidFromQuery(query: String?): String?{
        return query?.let { queryNotNull ->
            // query can be not related to TDUID
            if(queryNotNull.contains(TDUID,true)){
                try{
                    return getHashMapFromQuery(queryNotNull).filter { it.key.equals(TDUID,true) }.values.firstOrNull()
                }catch (e: java.lang.Exception){
                    // add some logs
                }

            }
            null
        }
    }

    private fun getHashMapFromQuery(query: String): Map<String, String> {
        val queryValues: MutableMap<String, String> = LinkedHashMap()
        val pairs = query.split("&").toTypedArray()
        for (pair in pairs) {
            val idx = pair.indexOf("=")
            queryValues[URLDecoder.decode(pair.substring(0, idx), Charsets.UTF_8.name()).toLowerCase()] = URLDecoder.decode(pair.substring(idx + 1), Charsets.UTF_8.name())
        }
        return queryValues
    }

    internal fun Double.format(digits: Int) = "%.${digits}f".format(this)

}

