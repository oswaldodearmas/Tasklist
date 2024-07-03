package com.odearmas.tasklist.data

import android.provider.BaseColumns

data class Task(var name: String, var categoryId: Int, var done: Boolean = false, var taskId: Int = -1) {

    companion object {
        const val TABLE_TASK = "task"
        const val COLUMN_TASK_ID = "taskId"
        const val COLUMN_NAME = "name"
        const val COLUMN_DONE = "done"
        const val COLUMN_CATEGORY_ID = "categoryId"
        const val SQL_CREATE_TABLE_TASK =
            "CREATE TABLE $TABLE_TASK (" +
                    "$COLUMN_TASK_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NAME TEXT, " +
                    "$COLUMN_DONE INTEGER, " +
                    "$COLUMN_CATEGORY_ID INTEGER, "+
                    "CONSTRAINT fk_category " +
                    "FOREIGN KEY($COLUMN_CATEGORY_ID) " +
                    "REFERENCES ${Category.TABLE_CATEGORY}(${Category.COLUMN_CATEGORY_ID}) ON DELETE CASCADE)"
        const val SQL_DROP_TABLE_TASK = "DROP TABLE IF EXISTS $TABLE_TASK"
    }
}