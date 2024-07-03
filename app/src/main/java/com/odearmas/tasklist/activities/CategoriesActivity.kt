package com.odearmas.tasklist.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.odearmas.tasklist.R
import com.odearmas.tasklist.data.Category
import com.odearmas.tasklist.data.CategoryDAO
import com.odearmas.tasklist.data.Task
import com.odearmas.tasklist.data.TaskDAO

class CategoriesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_categories)





        /*val categoryDAO = CategoryDAO(this)

        var category0 = Category("Lista Mercado")
        var category1 = Category("Solicitudes")
        var category2 = Category("Programas")
        var category3 = Category("Diligencias")
        var category4 = Category("Otros")

        category0 = categoryDAO.insert(category0)
        category1 = categoryDAO.insert(category1)
        category2 = categoryDAO.insert(category2)
        category3 = categoryDAO.insert(category3)

        Log.i("CATEGORYOBJECT", category2.toString())
        Log.i("CATEGORY3", categoryDAO.find(3).toString())
        Log.i("CATEGORYALL", categoryDAO.findAll().toString())

        category2 = categoryDAO.update(category4,category2.categoryId)

        Log.i("CATEGORYOBJECT", category2.toString())
        Log.i("CATEGORY3", categoryDAO.find(3).toString())
        Log.i("CATEGORYALL", categoryDAO.findAll().toString())

        categoryDAO.delete(category3)

        Log.i("CATEGORYALL", categoryDAO.findAll().toString())

        val taskDAO = TaskDAO(this)

        var task0 = Task("Java",categoryDAO.find(3)!!.categoryId)
        var task1 = Task("PHP",categoryDAO.find(3)!!.categoryId)
        var task2 = Task("Kotlin",categoryDAO.find(3)!!.categoryId)
        var task3 = Task("Swift",categoryDAO.find(3)!!.categoryId)
        var task4 = Task("Otro Lenguaje",categoryDAO.find(3)!!.categoryId)

        task0 = taskDAO.insert(task0)
        task1 = taskDAO.insert(task1)
        task2 = taskDAO.insert(task2)
        task3 = taskDAO.insert(task3)

        Log.i("TASKOBJECT", task2.toString())
        Log.i("TASK3", taskDAO.find(3).toString())
        Log.i("TASKALL", taskDAO.findAll().toString())

        task2 = taskDAO.update(task4,task2.taskId)

        Log.i("TASKOBJECT", task2.toString())
        Log.i("TASK3", taskDAO.find(3).toString())
        Log.i("TASKALL", taskDAO.findAll().toString())

        taskDAO.delete(task3)

        Log.i("TASKALL", taskDAO.findAll().toString())*/
    }
}