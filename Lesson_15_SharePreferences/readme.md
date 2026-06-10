# Android SharedPreferences Tutorial

`SharedPreferences` is a key-value storage system used in Android to save small amounts of primitive data (Strings, Ints, Booleans, etc.) persistently. It's ideal for user settings, login tokens, or simple app states.

---

### 1. Initialization
To use SharedPreferences, you need to create or access a file. You can have multiple preference files by giving them unique names.

```kotlin
// In an Activity or where context is available
val pref = context.getSharedPreferences("my_user_data", Context.MODE_PRIVATE)
```

*   `"my_user_data"`: The name of the XML file stored on the device.
*   `Context.MODE_PRIVATE`: Ensures only your app can access this file.

---

### 2. Writing Data
To modify preferences, you must use an `Editor`. Modern Android development uses the `androidx.core.content.edit` KTX extension for a cleaner syntax.

```kotlin
// Using KTX extension (Recommended)
pref.edit { 
    putString("user_name", "Devesh")
    putInt("user_age", 25)
}
```
*Note: The KTX `.edit {}` block automatically calls `apply()` for you.*

---

### 3. Reading Data
Reading is direct. You must provide a **default value** in case the key doesn't exist.

```kotlin
val name = pref.getString("user_name", "Guest") // Returns "Guest" if key is missing
val age = pref.getInt("user_age", 0)
```

---

### 4. Deleting Data
You can remove a specific key or wipe the entire file.

```kotlin
pref.edit {
    remove("user_name") // Deletes one key
    // clear()         // Deletes everything in this file
}
```

---

### 5. Using in Jetpack Compose
In your `MainActivity.kt`, you integrated this into a UI. Here is the flow for a "Save" action:

```kotlin
@Composable
fun SharedPreferencesDemo() {
    val context = LocalContext.current
    var inputKey by remember { mutableStateOf("") }
    var inputValue by remember { mutableStateOf("") }

    Button(onClick = {
        val pref = context.getSharedPreferences("my_user_data", Context.MODE_PRIVATE)
        pref.edit { 
            putString(inputKey, inputValue) 
        }
        Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show()
    }) {
        Text("Save Data")
    }
}
```

---

### 🌟 Best Practice: Using a Singleton Manager

As your app grows, calling `getSharedPreferences` manually in every Activity or ViewModel leads to code duplication and potential bugs (like using the wrong file name).

The **best practice** is to wrap `SharedPreferences` in a **Singleton Class**. This centralizes your data logic and ensures you use the `applicationContext` to prevent memory leaks.

Here is the professional implementation:

```kotlin
// MySharedPreferencesManager.kt
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

        // Thread-safe Singleton pattern
        fun getInstance(context: Context): MySharedPreferencesManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MySharedPreferencesManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

    // Simplified Save Methods
    fun saveString(key: String, value: String) {
        sharedPreferences.edit { putString(key, value) }
    }

    fun saveInt(key: String, value: Int) {
        sharedPreferences.edit { putInt(key, value) }
    }

    // Simplified Read Methods
    fun getString(key: String, defaultValue: String? = null): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    // Utility Methods
    fun delete(key: String) = sharedPreferences.edit { remove(key) }
    
    fun clear() = sharedPreferences.edit { clear() }
}
```

#### Why use this approach?
1.  **Single Source of Truth**: The file name (`"my_user_data"`) is defined in exactly one place.
2.  **Type Safety**: You can create specific methods for your data types, reducing the chance of `ClassCastException`.
3.  **Memory Leak Prevention**: By using `context.applicationContext`, the manager doesn't hold onto an Activity's lifecycle.
4.  **Cleaner UI Code**: Saving data becomes a single readable line:
    `MySharedPreferencesManager.getInstance(context).saveString("username", "Devesh")`

---

### Summary Tips:
1.  **`apply()` vs `commit()`**: `apply()` (used by KTX) is asynchronous and safer for the UI thread. `commit()` is synchronous and returns a boolean.
2.  **Strings Only?**: No! You can store `StringSet`, `Int`, `Long`, `Float`, and `Boolean`.
3.  **EncryptedSharedPreferences**: For sensitive data like passwords, use the Security library's encrypted version of this API.
