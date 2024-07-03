package com.odearmas.tasklist.data

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import com.odearmas.tasklist.utils.DatabaseManager

class TaskDAO(context: Context) {

    private val databaseManager: DatabaseManager = DatabaseManager(context)

    fun insert(task: Task): Task {
        val db = databaseManager.writableDatabase

        val values = ContentValues()
        values.put(Task.COLUMN_NAME, task.name)
        values.put(Task.COLUMN_DONE, task.done)
        values.put(Task.COLUMN_CATEGORY_ID, task.categoryId)

        val newRowId = db.insert(Task.TABLE_TASK, null, values)
        task.taskId = newRowId.toInt()
        return task
    }

    fun update(task: Task, id: Int): Task {
        val db = databaseManager.writableDatabase

        val values = ContentValues()
        values.put(Task.COLUMN_NAME, task.name)
        values.put(Task.COLUMN_DONE, task.done)
        values.put(Task.COLUMN_CATEGORY_ID, task.categoryId)

        val updatedRows = db.update(
            Task.TABLE_TASK,
            values,
            "${Task.COLUMN_TASK_ID} = $id",
            null
        )
        return task
    }

    fun delete(task: Task) {
        val db = databaseManager.writableDatabase

        val deletedRows = db.delete(
            Task.TABLE_TASK,
            "${Task.COLUMN_TASK_ID} = ${task.taskId}", null
        )
    }

    fun find(id: Int): Task? {
        val db = databaseManager.readableDatabase

        val projection = arrayOf(
            Task.COLUMN_NAME,
            Task.COLUMN_CATEGORY_ID,
            Task.COLUMN_DONE,
            Task.COLUMN_TASK_ID
        )

        val cursor = db.query(
            Task.TABLE_TASK,                        // The table to query
            projection,                   // The array of columns to return (pass null to get all)
            "${Task.COLUMN_TASK_ID} = $id",      // The columns for the WHERE clause
            null,                         // The values for the WHERE clause
            null,                            // don't group the rows
            null,                             // don't filter by row groups
            null                             // The sort order
        )

        var task: Task? = null
        if (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_TASK_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME))
            val category = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_CATEGORY_ID))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_DONE)) == 1
            task = Task(name, category, done, id)
        }
        cursor.close()
        db.close()
        return task
    }

    fun findAll(): List<Task> {
        val db = databaseManager.readableDatabase

        val projection = arrayOf(
            Task.COLUMN_NAME,
            Task.COLUMN_CATEGORY_ID,
            Task.COLUMN_DONE,
            Task.COLUMN_TASK_ID
        )

        val cursor = db.query(
            Task.TABLE_TASK,                        // The table to query
            projection,                    // The array of columns to return (pass null to get all)
            null,                            // The columns for the WHERE clause
            null,                         // The values for the WHERE clause
            null,                            // don't group the rows
            null,                             // don't filter by row groups
            null                             // The sort order
        )

        val tasks = mutableListOf<Task>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_TASK_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME))
            val category = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_CATEGORY_ID))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_DONE)) == 1
            val task = Task(name, category, done, id)
            task.taskId = id
            tasks.add(task)
        }
        cursor.close()
        db.close()
        return tasks
    }
}