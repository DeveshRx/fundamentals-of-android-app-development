package com.devesh.dbtutoriall.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * DATABASE CLASS: The main entry point for the Room database.
 * 
 * 1. @Database: Specifies the entities (tables) and the version.
 * 2. FlowerDatabase: Must be abstract and extend RoomDatabase.
 * 3. flowerDao(): An abstract method to provide access to our DAO.
 */
@Database(entities = [Flower::class], version = 1, exportSchema = false)
abstract class FlowerDatabase : RoomDatabase() {
    
    // Abstract method that returns our interface for database operations
    abstract fun flowerDao(): FlowerDao

    companion object {
        /**
         * SINGLETON PATTERN: Creating a database instance is expensive.
         * We use this pattern to ensure only ONE instance of the database exists 
         * across the entire application lifecycle.
         */
        
        // @Volatile ensures the instance is always visible to all threads
        @Volatile
        private var INSTANCE: FlowerDatabase? = null

        fun getDatabase(context: Context): FlowerDatabase {
            // If INSTANCE is not null, return it; otherwise, create it
            return INSTANCE ?: synchronized(this) {
                // synchronized(this) prevents multiple threads from creating the DB at the same time
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlowerDatabase::class.java,
                    "flower_db" // The name of the database file
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
