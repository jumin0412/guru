package com.example.guru

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class UserDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "users"
        private const val COL_EMAIL = "email"
        private const val COL_PASSWORD = "password"
        private const val COL_NAME = "name"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COL_EMAIL TEXT PRIMARY KEY,
                $COL_PASSWORD TEXT,
                $COL_NAME TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertUser(email: String, password: String, name: String): Boolean {
        val db = this.writableDatabase

        val cleanEmail = email.trim().lowercase()
        val cleanPassword = password.trim()
        val cleanName = name.trim()

        // 이메일 중복 확인
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COL_EMAIL = ?", arrayOf(cleanEmail))
        if (cursor.count > 0) {
            cursor.close()
            return false // 이미 존재
        }
        cursor.close()

        val values = ContentValues().apply {
            put(COL_EMAIL, cleanEmail)
            put(COL_PASSWORD, cleanPassword)
            put(COL_NAME, cleanName)
        }

        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result != -1L
    }

    fun checkUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val cleanEmail = email.trim().lowercase()
        val cleanPassword = password.trim()
        val cursor = db.rawQuery(
                "SELECT * FROM $TABLE_NAME WHERE $COL_EMAIL = ? AND $COL_PASSWORD = ?",
        arrayOf(cleanEmail, cleanPassword)
        )
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }
}
