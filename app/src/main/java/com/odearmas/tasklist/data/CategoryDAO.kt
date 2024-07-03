package com.odearmas.tasklist.data

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import com.odearmas.tasklist.utils.DatabaseManager

class CategoryDAO(context: Context) {

    private val databaseManager: DatabaseManager = DatabaseManager(context)

    fun insert(category: Category) :Category{
        val db = databaseManager.writableDatabase

        val values = ContentValues()
        values.put(Category.COLUMN_CATEGORY_NAME, category.categoryName)
        values.put(Category.COLUMN_TASKS_COUNT, category.tasksCount)

        val newRowId = db.insert(Category.TABLE_CATEGORY, null, values)
        category.categoryId = newRowId.toInt()
        return category
    }

    fun update(category: Category,id: Int) :Category{
        val db = databaseManager.writableDatabase

        val values = ContentValues()
        values.put(Category.COLUMN_CATEGORY_NAME, category.categoryName)
        values.put(Category.COLUMN_TASKS_COUNT, category.tasksCount)

        val updatedRows = db.update(
            Category.TABLE_CATEGORY,
            values,
            "${Category.COLUMN_CATEGORY_ID} = $id",
            null)
        return category
    }

    fun delete(category: Category) {
        val db = databaseManager.writableDatabase

        val deletedRows = db.delete(
            Category.TABLE_CATEGORY,
            "${Category.COLUMN_CATEGORY_ID} = ${category.categoryId}",
            null)
    }

    fun find(id: Int): Category? {
        val db = databaseManager.readableDatabase

        val projection = arrayOf(Category.COLUMN_CATEGORY_NAME,
            Category.COLUMN_TASKS_COUNT, Category.COLUMN_CATEGORY_ID
        )

        val cursor = db.query(
            Category.TABLE_CATEGORY,                        // The table to query
            projection,                   // The array of columns to return (pass null to get all)
            "${Category.COLUMN_CATEGORY_ID} = $id",      // The columns for the WHERE clause
            null,                         // The values for the WHERE clause
            null,                            // don't group the rows
            null,                             // don't filter by row groups
            null                             // The sort order
        )

        var category:Category? = null
        if (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(Category.COLUMN_CATEGORY_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Category.COLUMN_CATEGORY_NAME))
            val count = cursor.getInt(cursor.getColumnIndexOrThrow(Category.COLUMN_TASKS_COUNT))
            category = Category(name,count,id)
        }
        cursor.close()
        db.close()
        return category
    }

    fun findAll(): List<Category> {
        val db = databaseManager.readableDatabase

        val projection = arrayOf(Category.COLUMN_CATEGORY_NAME,
            Category.COLUMN_TASKS_COUNT,Category.COLUMN_CATEGORY_ID)

        val cursor = db.query(
            Category.TABLE_CATEGORY,                        // The table to query
            projection,                   // The array of columns to return (pass null to get all)
            null,                           // The columns for the WHERE clause
            null,                         // The values for the WHERE clause
            null,                            // don't group the rows
            null,                             // don't filter by row groups
            null                             // The sort order
        )

        val categories = mutableListOf<Category>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(Category.COLUMN_CATEGORY_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(Category.COLUMN_CATEGORY_NAME))
            val count = cursor.getInt(cursor.getColumnIndexOrThrow(Category.COLUMN_TASKS_COUNT))
            val category = Category(name,count,id)
            categories.add(category)
        }
        cursor.close()
        db.close()
        return categories
    }
}