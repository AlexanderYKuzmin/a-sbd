package com.example.a_sbd.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.a_sbd.data.database.model.ChatContactDb
import com.example.a_sbd.data.database.model.MessageDb
import java.io.File

@Database(entities = [ChatContactDb::class, MessageDb::class], version = 4, exportSchema = false)
abstract class ASBDDatabase : RoomDatabase(){
    companion object {

        private var db: ASBDDatabase? = null
        private const val DB_NAME = "sample.db"
        private val LOCK = Any()

        fun getInstance(context: Context): ASBDDatabase {
            synchronized(LOCK) {
                db?.let { return it }
                val instance =
                    Room.databaseBuilder(
                        context,
                        ASBDDatabase::class.java,
                        DB_NAME
                    )
                        //.createFromAsset("database/asbd_2.db")
                        .fallbackToDestructiveMigration()
                        .build()
                db = instance
                return instance
            }
        }
    }

    abstract fun chatContactsDao(): ChatContactsDao
}