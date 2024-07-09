package com.odearmas.tasklist.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.odearmas.tasklist.R
import com.odearmas.tasklist.adapters.CategoryAdapter
import com.odearmas.tasklist.data.entities.Category
import com.odearmas.tasklist.data.providers.CategoryDAO
import com.odearmas.tasklist.data.providers.TaskDAO
import com.odearmas.tasklist.databinding.ActivityCategoriesBinding
import com.odearmas.tasklist.databinding.AddCategoryDialogBinding
import com.odearmas.tasklist.databinding.EditCategoryDialogBinding

class CategoriesActivity : AppCompatActivity() {

    lateinit var categoryBinding: ActivityCategoriesBinding
    lateinit var categoryDAO: CategoryDAO
    lateinit var taskDAO: TaskDAO
    lateinit var categoryMutableList: MutableList<Category>
    lateinit var categoryAdapter: CategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoryBinding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(categoryBinding.root)

        supportActionBar?.title=getString(R.string.categories_title)
        supportActionBar?.setBackgroundDrawable(getDrawable(R.color.menu))

        categoryDAO = CategoryDAO(this)
        taskDAO = TaskDAO(this)
        categoryMutableList = categoryDAO.findAll().toMutableList()

        //Log.i("CATEGORIES", categoryMutableList.toString())

        initView() //Initialise adapter and pass it to the reciclerView

/*        var category1 = Category("Lista Mercado")
        var category2 = Category("Proyecto Desarrollo")
        var category3 = Category("Diligencias Pendientes")
        var category4 = Category("Documentos TGSS")
        var category5 = Category("Listado de Alumnos")
        var category6 = Category("Sitios Web")
        var category7 = Category("Tecnologías")
        var category8 = Category("Vacaciones")

        category1 = categoryDAO.insert(category1)
        category2 = categoryDAO.insert(category2)
        category3 = categoryDAO.insert(category3)
        category4 = categoryDAO.insert(category4)
        category5 = categoryDAO.insert(category5)
        category6 = categoryDAO.insert(category6)
        category7 = categoryDAO.insert(category7)
        category8 = categoryDAO.insert(category8)

        var task1 = Task("Leche",1)
        var task2 = Task("Queso",1)
        var task3 = Task("Carne",1)
        var task4 = Task("Pollo",1)
        var task5 = Task("Pescado",1)
        var task6 = Task("Pasta",1)
        var task7 = Task("Hummus",1)
        var task8 = Task("Cerveza",1)

        task1 = taskDAO.insert(task1)
        task2 = taskDAO.insert(task2)
        task3 = taskDAO.insert(task3)
        task4 = taskDAO.insert(task4)
        task5 = taskDAO.insert(task5)
        task6 = taskDAO.insert(task6)
        task7 = taskDAO.insert(task7)
        task8 = taskDAO.insert(task8)

        val task9 = Task("Pipas",1,true)
        taskDAO.update(task9,8)

        taskDAO.insert(Task("GoFit", 2))
        taskDAO.insert(Task("Ecatalogue", 2, true))

        taskDAO.insert(Task("Task 1", 3,true))
        taskDAO.insert(Task("Task 2", 3,true))
        taskDAO.insert(Task("Task 3", 3,true))
        taskDAO.insert(Task("Task 4", 3,true))
        taskDAO.insert(Task("Task 5", 3,true))
        taskDAO.insert(Task("Task 6", 3,true))
        taskDAO.insert(Task("Task 1", 3,true))*/

    }

    override fun onResume() { //return to this activity from another and update the view
        super.onResume()

        loadData() //update the adapter
    }

    private fun initView() {

        categoryAdapter = CategoryAdapter(this, categoryMutableList, {
            onCategoryItemClickListener(it)
        }, {
            onCategoryEditClickListener(it)
            //return@CategoryAdapter true
        },{
            onCategoryDeleteClickListener(it)
            //return@CategoryAdapter true
        })

        categoryBinding.recyclerView.adapter = categoryAdapter
        categoryBinding.recyclerView.layoutManager = LinearLayoutManager(this)

    }

    private fun loadData(){
        categoryMutableList = categoryDAO.findAll().toMutableList()
        categoryAdapter.updateItems(categoryMutableList)
    }

    private fun searchCategory(searchText: String) {
            try {
                categoryMutableList = categoryDAO.findCategoryByName(searchText).toMutableList()
                categoryAdapter.updateItems(categoryMutableList)
                // Log.i("HTTP", "${result.results}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.categories_menu,menu)
        val searchViewItem = menu.findItem(R.id.menu_search_lists)
        val searchView = searchViewItem.actionView as SearchView


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(newText: String?): Boolean {

                searchViewItem.collapseActionView()
                if (newText != null) {
                    searchCategory(newText)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchCategory(newText)
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
                addCategory()
                loadData()
                true
            }
            else -> {super.onOptionsItemSelected(item)}
        }
    }

    private fun onCategoryItemClickListener(position:Int) {
        val category: Category = categoryMutableList[position]
        val intent = Intent(this, TasksActivity::class.java)
        intent.putExtra("CATEGORY_ID", category.categoryId)
        startActivity(intent)
    }

    private fun onCategoryEditClickListener(position:Int) {
        editCategory(position)
    }

    private fun onCategoryDeleteClickListener(position:Int) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("WARNING!")
        builder.setMessage("You are about to delete the list \"${categoryMutableList[position].categoryName}\"! Are you sure?")

        builder.setPositiveButton("Delete") { dialog, which ->
            // Aquí va el código para borrar la lista
            deleteCategory(position)
        }

        builder.setNegativeButton("Return") { dialog, which ->
            // No se hace nada, se cierra el diálogo
        }

        builder.show()

    }

    private fun addCategory() {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val binding: AddCategoryDialogBinding = AddCategoryDialogBinding.inflate(layoutInflater)
        dialogBuilder.setView(binding.root)

        dialogBuilder.setTitle(R.string.add_category_title)
        dialogBuilder.setIcon(R.drawable.ic_add_category1)
        dialogBuilder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        dialogBuilder.setPositiveButton(R.string.add_category_button, null)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.show()

        // Need to move listener after show dialog to prevent dismiss
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val categoryName = binding.addCategoryTextField.editText?.text.toString()
            if (categoryName.isNotEmpty()) {
                val category = Category(categoryName)
                categoryDAO.insert(category)
                loadData()
                Toast.makeText(this, R.string.add_category_success_message, Toast.LENGTH_LONG).show()
                alertDialog.dismiss()
            } else {
                binding.addCategoryTextField.error = getString(R.string.add_category_empty_error)
            }
        }
    }

    private fun editCategory(position: Int) {
        val category = categoryMutableList[position]

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val binding: EditCategoryDialogBinding = EditCategoryDialogBinding.inflate(layoutInflater)
        dialogBuilder.setView(binding.root)

        binding.editCategoryTextField.editText?.setText(category.categoryName)

        dialogBuilder.setTitle(R.string.edit_category_title)
        dialogBuilder.setIcon(R.drawable.ic_edit)
        dialogBuilder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        dialogBuilder.setPositiveButton(R.string.edit_category_button, null)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val categoryName = binding.editCategoryTextField.editText?.text.toString()
            if (categoryName.isNotEmpty()) {
                categoryDAO.update(categoryName, category.categoryId)
                loadData()
                Toast.makeText(this, R.string.edit_category_success_message, Toast.LENGTH_LONG).show()
                alertDialog.dismiss()
            } else {
                binding.editCategoryTextField.error = getString(R.string.edit_category_empty_error)
            }
        }



    }

    private fun deleteCategory(position: Int) {
/*        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val binding: DeleteCategoryDialogBinding = DeleteCategoryDialogBinding.inflate(layoutInflater)
        dialogBuilder.setView(binding.root)

        dialogBuilder.setTitle(R.string.delete_category_title)
        dialogBuilder.setIcon(R.drawable.ic_delete)
        dialogBuilder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        dialogBuilder.setPositiveButton(R.string.delete_category_button, null)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.show()

        // Need to move listener after show dialog to prevent dismiss
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val categoryName = binding.deleteCategoryTextField.editText?.text.toString()
            if (categoryName.isNotEmpty()) {*/
                categoryDAO.delete(categoryMutableList[position].categoryId)
                loadData()
                Toast.makeText(this, R.string.delete_category_success_message, Toast.LENGTH_LONG).show()
/*                alertDialog.dismiss()
            } else {
                binding.deleteCategoryTextField.error = getString(R.string.delete_category_empty_error)
            }
        }*/



    }

}