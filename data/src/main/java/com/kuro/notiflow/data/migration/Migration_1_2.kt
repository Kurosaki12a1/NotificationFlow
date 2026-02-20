package com.kuro.notiflow.data.migration

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.kuro.notiflow.data.data_source.AppDatabase

/**
 * Adds data_retention_days column to settings_table.
 */
val Migration_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            "ALTER TABLE settings_table ADD COLUMN data_retention_days INTEGER NOT NULL DEFAULT ${AppDatabase.DEFAULT_RETENTION_DAYS}"
        )
    }
}
