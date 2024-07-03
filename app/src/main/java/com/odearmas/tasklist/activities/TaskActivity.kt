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

class TaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task)

       /* val taskDAO = TaskDAO(this)
        val categoryDAO = CategoryDAO(this)

        var category=Category("Lista Mercado")
        var task1 = Task("Leche", false, category.categoryId)
        var task2 = Task("Tabaco", false, category.categoryId)
        var task3 = Task("Fruta", false, category.categoryId)
        taskDAO.insert(task1)
        taskDAO.insert(task2)
        taskDAO.insert(task3)

        Log.i("DATABASE", task1.toString())
        Log.i("DATABASE", task2.toString())
        Log.i("DATABASE", task3.toString())

        task1.done = true
        taskDAO.update(task1)
        task3.done = true
        taskDAO.update(task3)

        Log.i("DATABASE", task1.toString())
        Log.i("DATABASE", task2.toString())
        Log.i("DATABASE", task3.toString())

        val task4 = taskDAO.find(task1.taskId)!!

        Log.i("DATABASE", task4.toString())

        val taskList = taskDAO.findAll()

        Log.i("DATABASE", taskList.toString())

        taskDAO.delete(task3)

        Log.i("DATABASE", taskDAO.findAll().toString())

        findViewById<Button>(R.id.categories_test_button).setOnClickListener {
            taskDAO.findAll()*/
     //   }
    }
}