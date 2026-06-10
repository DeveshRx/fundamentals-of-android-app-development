package deveshrx.sharepreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class MySharedPreferencesManager private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "my_user_data"

        @Volatile
        private var INSTANCE: MySharedPreferencesManager? = null

        fun getInstance(context: Context): MySharedPreferencesManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MySharedPreferencesManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

    // Create / Update (Overloaded for common types)
    fun saveString(key: String, value: String) {
        sharedPreferences.edit { putString(key, value) }
    }

    fun saveInt(key: String, value: Int) {
        sharedPreferences.edit { putInt(key, value) }
    }

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit { putBoolean(key, value) }
    }

    fun saveLong(key: String, value: Long) {
        sharedPreferences.edit { putLong(key, value) }
    }

    fun saveFloat(key: String, value: Float) {
        sharedPreferences.edit { putFloat(key, value) }
    }

    // Read
    fun getString(key: String, defaultValue: String? = null): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    // Delete
    fun delete(key: String) {
        sharedPreferences.edit { remove(key) }
    }

    // Get All Key-Values
    fun getAll(): Map<String, *> {
        return sharedPreferences.all
    }

    // Clear everything
    fun clear() {
        sharedPreferences.edit { clear() }
    }
}
