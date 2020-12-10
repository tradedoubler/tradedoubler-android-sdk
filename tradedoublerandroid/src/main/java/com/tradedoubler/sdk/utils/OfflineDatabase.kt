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

package com.tradedoubler.sdk.utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class OfflineDatabase(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE " + TABLE_Offline + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_URL + " TEXT" + ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_Offline")
        onCreate(db)
    }

    fun insertUrl(url: String): OfflineUrl {
        val db = this.writableDatabase
        val cValues = ContentValues()
        cValues.put(KEY_URL, url)
        val rowId = db.insert(TABLE_Offline, null, cValues)
        return OfflineUrl(rowId,url)
    }

    fun peekOldestUrl(): OfflineUrl?{
        val db = this.writableDatabase
        val cursor = db.query(
            TABLE_Offline,
            arrayOf(KEY_URL, KEY_ID),
            null,
            null,
            null,
            null,
            "$KEY_ID ASC",
            "1"
        )
        return if (cursor.moveToNext()) {
            val urlId =  cursor.getLong(cursor.getColumnIndex(KEY_ID))
            OfflineUrl(urlId, cursor.getString(cursor.getColumnIndex(KEY_URL))).apply {
                cursor.close()
                deleteUrl(urlId = urlId)
            }
        } else null
    }

    fun peekUrlById(urlId: Long): OfflineUrl? {
        val db = this.writableDatabase
        val cursor = db.query(
            TABLE_Offline,
            arrayOf(KEY_URL),
            "$KEY_ID=?",
            arrayOf(urlId.toString()),
            null,
            null,
            null,
            null
        )
        return if (cursor.moveToNext()) {
            OfflineUrl(urlId, cursor.getString(cursor.getColumnIndex(KEY_URL))).apply {
                cursor.close()
                deleteUrl(urlId = urlId)
            }
        } else null
    }

    fun deleteUrl(urlId: Long) {
        val db = this.writableDatabase
        db.delete(TABLE_Offline, "$KEY_ID = ?", arrayOf(urlId.toString()))
    }

    companion object {
        private const val DB_VERSION = 1
        private const val DB_NAME = "td_db"
        private const val TABLE_Offline = "offline"
        private const val KEY_ID = "id"
        private const val KEY_URL = "url"
    }

    data class OfflineUrl(val id: Long, val url: String)
}