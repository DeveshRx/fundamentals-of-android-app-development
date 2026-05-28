# Mastering File Input-Output in Modern Android: A Comprehensive Guide

Handling files in Android has evolved significantly over the years. Gone are the days of broad `READ_EXTERNAL_STORAGE` permissions and direct file path manipulations. Modern Android development emphasizes **Scoped Storage**, user privacy, and standardized APIs.

In this guide, we’ll explore the three primary ways to handle File I/O in a modern Android app, referencing the "My File IO" project.

---

## 1. Internal Storage: Keeping Data Private

Internal storage is the best place for data that should only be accessible by your app. Files stored here are automatically deleted when the app is uninstalled.

### Key Characteristics:
- **Private:** Other apps cannot access these files.
- **No Permissions Needed:** You don't need to request any system permissions.
- **Automatic Cleanup:** Files are removed on app uninstall.

### Implementation Reference (`FileSaveInternalActivity.kt`):

To save a file, use `openFileOutput()`:

```kotlin
val filename = "HelloWorld.txt"
val fileOutputStream = context.openFileOutput(filename, MODE_PRIVATE)

fileOutputStream.use { output ->
    output.write(userInput.toByteArray())
}
```

To read a file, use `openFileInput()`:

```kotlin
val filename = "HelloWorld.txt"
val fileInputStream = context.openFileInput(filename)

fileInputStream.bufferedReader().use {
    val content = it.readText()
}
```

---

## 2. MediaStore API: Working with Public Directories

If you want to save files like photos, videos, or documents to shared folders (e.g., `Downloads`, `Pictures`), the **MediaStore API** is the modern standard for Scoped Storage (Android 10+).

### Key Characteristics:
- **Shared Access:** Other apps can see these files (if they have permission).
- **Persistent:** Files remain even after the app is uninstalled.
- **Specific Folders:** You can only contribute to specific collections like `Downloads/` or `Pictures/`.

### Implementation Reference (`MediaStoreAPIActivity.kt`):

Saving to the `Downloads` folder involves creating `ContentValues` and using a `ContentResolver`:

```kotlin
val values = ContentValues().apply {
    put(MediaStore.MediaColumns.DISPLAY_NAME, "My_New_File.txt")
    put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
    put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/")
}

val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
uri?.let {
    contentResolver.openOutputStream(it)?.use { stream ->
        stream.write(content.toByteArray())
    }
}
```

Reading requires querying the MediaStore to find the URI of the file:

```kotlin
val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ?"
val args = arrayOf(name)

val cursor = contentResolver.query(
    MediaStore.Downloads.EXTERNAL_CONTENT_URI, 
    arrayOf(MediaStore.MediaColumns._ID), 
    selection, args, null
)
// Extract ID and convert to URI using ContentUris.withAppendedId()
```

---

## 3. Storage Access Framework (SAF): Giving Users Control

The Storage Access Framework (SAF) lets users pick exactly where a file should be saved or which file should be opened. It provides a system-controlled file picker.

### Key Characteristics:
- **User Choice:** The user decides the location and filename.
- **Deep Integration:** Works with cloud storage providers (Google Drive, Dropbox).
- **No Permissions Needed:** The user’s selection grants your app temporary access.

### Implementation Reference (`SAFActivity.kt`):

In modern Compose apps, we use `rememberLauncherForActivityResult`:

**To Save a File:**

```kotlin
val createFileLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.CreateDocument("text/plain")
) { uri ->
    uri?.let {
        contentResolver.openOutputStream(it)?.use { stream ->
            stream.write(input.toByteArray())
        }
    }
}

// Trigger it:
createFileLauncher.launch("My_New_File.txt")
```

**To Open a File:**

```kotlin
val openFileLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocument()
) { uri ->
    uri?.let {
        val content = contentResolver.openInputStream(it)?.bufferedReader()?.use { reader ->
            reader.readText()
        }
    }
}

// Trigger it:
openFileLauncher.launch(arrayOf("text/plain"))
```

---

## Conclusion: Which one should you use?

| Use Case | Recommended API |
| :--- | :--- |
| **Private app data, caches, settings** | Internal Storage |
| **Media files (Images/Videos) for public use** | MediaStore API |
| **Documents, user-selected downloads/backups** | Storage Access Framework (SAF) |

By following these modern patterns, you ensure your app is compatible with the latest Android security requirements while providing a smooth user experience. Happy coding!
