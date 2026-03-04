package com.example.learningai.localDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Version 3 se 4 kiya kyunki sessionId column add hua hai
@Database(
    entities = [
        QuestionEntity::class,
        UserEntity::class,
        ChatMessageEntity::class
    ],
    version = 4, // <-- Version 4 zaroori hai
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao
    abstract fun userDao(): UserDao
    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "learning_ai_db"
                )
                    // Schema change hone par purana data clear karke crash hone se bachayega
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}