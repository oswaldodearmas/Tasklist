package com.odearmas.tasklist.activities

import android.os.Bundle
import android.util.Log
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
import com.odearmas.tasklist.databinding.EditCategoryDialogBinding
import com.odearmas.tasklist.databinding.EditTaskDialogBinding
import com.odearmas.tasklist.databinding.ItemTaskBinding

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
        categoryDAO = CategoryDAO(this)
        taskDAO = TaskDAO(this)

        this.categoryId = intent.getIntExtra("CATEGORY_ID", -1)
        supportActionBar?.title= categoryDAO.find(this.categoryId)?.categoryName.toString()
        supportActionBar?.setBackgroundDrawable(getDrawable(R.color.taskItems))
        //taskMutableList = taskDAO.findTaskByCategory(categoryId).toMutableList()


        initView()

    }

    private fun initView() {
        taskAdapter = TaskAdapter(this, taskMutableList, {
            //onTaskItemClickListener(it)
        }, {
            onTaskEditClickListener(it)
        },{
            onTaskDeleteClickListener(it)
        },{
            onTaskCheckBoxClickListener(it)
        })

        taskBinding.taskRecyclerView.adapter = taskAdapter
        taskBinding.taskRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun onTaskCheckBoxClickListener(position: Int) {

        val task = taskMutableList[position]
        //Log.i("DONE-TASK-ID", "Task id = ${task.taskId}")
        //Log.i("DONE-TASK-POSITION", "Task position = ${position}")
        task.done = !task.done
        taskDAO.update(task, task.taskId)
        //taskAdapter.notifyItemChanged(position)
        //taskAdapter.notifyDataSetChanged()
        loadData()
    }

    override fun onResume() { //return to this activity from another and update the view
        super.onResume()

        loadData() //update the adapter
    }

    private fun loadData(){
        taskMutableList = taskDAO.findTaskByCategory(this.categoryId).toMutableList()
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

            R.id.new_task_button -> {
                addTask()
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

    private fun onTaskEditClickListener(position: Int) {
        val task = taskMutableList[position]

        //Log.i("EDIT-TASK-ID", "Task id = ${task.taskId}")
        //Log.i("EDIT-TASK-POSITION", "Task position = ${position}")

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val binding: EditTaskDialogBinding = EditTaskDialogBinding.inflate(layoutInflater)
        dialogBuilder.setView(binding.root)

        binding.editTaskTextField.editText?.setText(task.name)

        dialogBuilder.setTitle(R.string.edit_task_title)
        dialogBuilder.setIcon(R.drawable.ic_edit)
        dialogBuilder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        dialogBuilder.setPositiveButton(R.string.edit_category_button, null)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val taskName = binding.editTaskTextField.editText?.text.toString()
            if (taskName.isNotEmpty()) {
                task.name = taskName
                taskDAO.update(task, task.taskId)
                loadData()
                Toast.makeText(this, R.string.edit_task_success_message, Toast.LENGTH_LONG).show()
                alertDialog.dismiss()
            } else {
                binding.editTaskTextField.error = getString(R.string.edit_category_empty_error)
            }
        }}
    private fun onTaskDeleteClickListener(position:Int) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("WARNING!")
        builder.setMessage("You are about to delete the task \"${taskMutableList[position].name}\"! Are you sure?")

        builder.setPositiveButton("Delete") { dialog, which ->
            // Aquí va el código para borrar la lista
            taskDAO.delete(taskMutableList[position].taskId)
            loadData()
            Toast.makeText(this, R.string.delete_task_success_message, Toast.LENGTH_LONG).show()
        }

        builder.setNegativeButton("Return") { dialog, which ->
            // No se hace nada, se cierra el diálogo
        }

        builder.show()

    }
}