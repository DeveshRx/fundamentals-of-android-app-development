package com.devesh.dbtutoriall.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ENTITY CLASS: This represents a table in the Room database.
 * 
 * Think of this class as a blueprint for a row in your "Flower" table.
 * Each instance of this class will be a single row in the database.
 */
@Entity
data class Flower(
    /**
     * PRIMARY KEY: Every table needs a unique way to identify each row.
     * 'autoGenerate = true' tells Room to automatically create a unique ID for every new flower we add.
     * We initialize it to 0 because Room will overwrite it with the real ID when saving.
     */
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    
    // The name of the flower (e.g., "Rose")
    var name: String,
    
    // The color of the flower (e.g., "Red")
    var color: String
)
