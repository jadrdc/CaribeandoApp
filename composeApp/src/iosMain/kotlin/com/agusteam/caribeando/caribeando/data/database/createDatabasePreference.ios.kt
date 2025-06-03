@file:OptIn(ExperimentalForeignApi::class)
package com.agusteam.caribeando.data.database

import kotlinx.cinterop.ExperimentalForeignApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.agusteam.caribeando.data.util.DATA_STORE_FILE_NAME
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
fun createDataStore(): DataStore<Preferences> {
    return createDataStore {
        val directory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        requireNotNull(directory).path + "/$DATA_STORE_FILE_NAME"
    }
}
