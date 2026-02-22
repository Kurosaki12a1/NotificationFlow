package com.kuro.notiflow.domain

object Constants {

    object App {
        const val DEVELOPER = "Huynh Minh Thinh"
        const val LICENCE = "Apache Licence v2.0"
        const val PERMISSION_TAG = "Notification_Permission"
        const val GITHUB_URI = "https://github.com/Kurosaki12a1/NotificationFlow"
        const val ISSUES_URI = "https://github.com/Kurosaki12a1/NotificationFlow/issues"
    }

    object Database {
        const val NAME = "notification_flow_db"
        const val SETTINGS_TABLE = "settings_table"
        const val NOTIFICATION_TABLE = "notification_table"
        const val SETTINGS_ID = 1

        const val COLUMN_ID = "id"
        const val COLUMN_LANGUAGE = "language"
        const val COLUMN_THEME_COLORS = "theme_colors"
        const val COLUMN_COLORS_TYPE = "colors_type"
        const val COLUMN_DYNAMIC_COLOR = "dynamic_color"
        const val COLUMN_SECURE_MODE = "secure_mode"
        const val COLUMN_DATA_RETENTION_DAYS = "data_retention_days"

        const val INIT_SETTINGS_SQL =
            "INSERT INTO $SETTINGS_TABLE (" +
                "$COLUMN_ID, $COLUMN_LANGUAGE, $COLUMN_THEME_COLORS, $COLUMN_COLORS_TYPE, " +
                "$COLUMN_DYNAMIC_COLOR, $COLUMN_SECURE_MODE, $COLUMN_DATA_RETENTION_DAYS" +
                ") VALUES ($SETTINGS_ID,'DEFAULT', 'DEFAULT', 'BLUE', 0, 0, 90)"
    }

    object Delay {
        const val MARQUEE_LOADING_TEXT = 1500
    }

    object DateFormat {
        const val TIME_SHORT = "HH:mm - dd/MM/yyyy"
        const val TIME_DETAIL = "EEEE, dd/MM/yyyy HH:mm"
    }

    object Home {
        const val OVERVIEW_KEY = "overview"
        const val STATISTIC_KEY = "statistic"
        const val RECENT_NOTIFICATION = "recent_notifications"
    }

    object Details {
        const val GENERAL_KEY = "general"
        const val DETAIL_KEY = "details"
        const val ACTION_KEY = "actions"
    }

    object Notifications {
        const val PAGE_SIZE = 50
        const val MIN_INSERT_INTERVAL_MILLIS = 60_000L
    }

    object Export {
        const val BASE_FILE_NAME = "notification_flow"
        const val FILE_EXTENSION = "csv"
        const val TIMESTAMP_PATTERN = "dd_MM_yyyy_HH_mm"
        const val TIME_FORMAT_PATTERN = "dd/MM/yy HH:mm"
    }

    object Settings {
        const val DEFAULT_RETENTION_DAYS = 90
        const val MAX_RETENTION_DAYS = 90
        const val MIN_RETENTION_DAYS = 1
    }

    object DataStore {
        const val NAME = "onboarding_prefs"
        const val KEY_FIRST_LAUNCH = "first_launch"
    }

    object Time {
        const val MILLIS_PER_SECOND = 1000L
        const val SECONDS_PER_MINUTE = 60
        const val MINUTES_PER_HOUR = 60
        const val HOURS_PER_DAY = 24
        const val DAYS_PER_WEEK = 7
        const val DAYS_PER_MONTH = 30
        const val DAYS_PER_YEAR = 365
        const val MILLIS_PER_DAY = 86_400_000L
        const val END_OF_DAY_OFFSET_MILLIS = 1L
    }
}
