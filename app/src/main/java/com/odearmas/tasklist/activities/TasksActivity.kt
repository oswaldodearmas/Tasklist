package com.odearmas.tasklist.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.odearmas.tasklist.R
import com.odearmas.tasklist.adapters.CategoryAdapter
import com.odearmas.tasklist.adapters.TaskAdapter
import com.odearmas.tasklist.data.entities.Category
import com.odearmas.tasklist.data.entities.Task
import com.odearmas.tasklist.data.providers.CategoryDAO
import com.odearmas.tasklist.data.providers.TaskDAO
import com.odearmas.tasklist.databinding.ActivityCategoriesBinding
import com.odearmas.tasklist.databinding.ActivityTaskBinding
import com.odearmas.tasklist.databinding.AddCategoryDialogBinding
import com.odearmas.tasklist.databinding.AddTaskDialogBinding

class TasksActivity : AppCompatActivity() {

    lateinit var taskBinding: ActivityTaskBinding
    lateinit var categoryDAO: CategoryDAO
    lateinit var taskDAO: TaskDAO
    var taskMutableList: MutableList<Task> = mutableListOf()
    lateinit var taskAdapter: TaskAdapter
    var categoryId : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        taskBinding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(taskBinding.root)

        supportActionBar?.title=getString(R.string.tasks_title)
        supportActionBar?.setBackgroundDrawable(getDrawable(R.color.menu))
        this.categoryId = intent.getIntExtra("CATEGORY_ID", -1)
        categoryDAO = CategoryDAO(this)
        taskDAO = TaskDAO(this)
        //taskMutableList = taskDAO.findTaskByCategory(categoryId).toMutableList()

        initView()
        //On Resume se ejecutará después del OnCreate
    }

    private fun initView() {
        taskAdapter = TaskAdapter(this, taskMutableList, {
            //onTaskItemClickListener(it)
        }, {
            //onTaskEditClickListener(it)
        },{
            //onTaskDeleteClickListener(it)
        })

        taskBinding.taskRecyclerView.adapter = taskAdapter
        taskBinding.taskRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() { //return to this activity from another and update the view
        super.onResume()

        loadData() //update the adapter
    }

    private fun loadData(){
        taskMutableList = taskDAO.findTaskByCategory(categoryId).toMutableList()
        taskAdapter.updateItems(taskMutableList)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.tasks_menu,menu)
        val searchTaskViewItem = menu.findItem(R.id.menu_search_tasks)
        val searchView = searchTaskViewItem.actionView as SearchView


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(newText: String?): Boolean {

                searchTaskViewItem.collapseActionView()
                if (newText != null) {
                    searchTask(newText)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchTask(newText)
                }
                return true
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){

            R.id.new_category_button -> {
                //categoryDAO.insert(Category("Esto es una prueba"))
                addTask()
                loadData()
                true
            }
            else -> {super.onOptionsItemSelected(item)}
        }
    }

    private fun searchTask(searchText: String) {
        try {
            taskMutableList = taskDAO.findTaskByName(searchText).toMutableList()
            taskAdapter.updateItems(taskMutableList)
            // Log.i("HTTP", "${result.results}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addTask() {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val binding: AddTaskDialogBinding = AddTaskDialogBinding.inflate(layoutInflater)
        dialogBuilder.setView(binding.root)

        dialogBuilder.setTitle(R.string.add_task_title)
        dialogBuilder.setIcon(R.drawable.ic_add_category2)
        dialogBuilder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        dialogBuilder.setPositiveButton(R.string.add_task_button, null)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.show()

        // Need to move listener after show dialog to prevent dismiss
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val taskName = binding.addTaskTextField.editText?.text.toString()
            //val categoryId = intent.getIntExtra("CATEGORY_ID", -1)
            if (taskName.isNotEmpty()) {
                val task = Task(taskName,categoryId)
                taskDAO.insert(task)
                loadData()
                Toast.makeText(this, R.string.add_category_success_message, Toast.LENGTH_LONG).show()
                alertDialog.dismiss()
            } else {
                binding.addTaskTextField.error = getString(R.string.add_task_empty_error)
            }
        }
    }
}