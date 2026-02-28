package com.kuro.notiflow.data.data_source.migration

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.kuro.notiflow.domain.Constants

val Migration_2_3: Migration = object : Migration(2, 3) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            "CREATE TABLE IF NOT EXISTS ${Constants.Database.BOOKMARK_RULE_TABLE} (" +
                "${Constants.Database.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "${Constants.Database.COLUMN_RULE_PACKAGE_NAME} TEXT, " +
                "${Constants.Database.COLUMN_RULE_KEYWORD} TEXT NOT NULL, " +
                "${Constants.Database.COLUMN_RULE_MATCH_FIELD} TEXT NOT NULL, " +
                "${Constants.Database.COLUMN_RULE_MATCH_TYPE} TEXT NOT NULL, " +
                "${Constants.Database.COLUMN_RULE_IS_ENABLED} INTEGER NOT NULL" +
                ")"
        )
    }
}
