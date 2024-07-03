package com.odearmas.tasklist.data

import android.provider.BaseColumns

data class Category(var categoryName: String, var tasksCount: Int=0, var categoryId: Int = -1) {

    companion object {
        const val TABLE_CATEGORY = "category"
        const val COLUMN_CATEGORY_NAME = "categoryName"
        const val COLUMN_TASKS_COUNT = "tasksCount"
        const val COLUMN_CATEGORY_ID = "categoryId"
        const val SQL_CREATE_TABLE_CATEGORY =
            "CREATE TABLE $TABLE_CATEGORY (" +
                    "$COLUMN_CATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_CATEGORY_NAME TEXT," +
                    "$COLUMN_TASKS_COUNT INTEGER)"
        const val SQL_DROP_TABLE_CATEGORY = "DROP TABLE IF EXISTS $TABLE_CATEGORY"
    }
}
