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
import androidx.recyclerview.widget.RecyclerView
import com.odearmas.tasklist.R
import com.odearmas.tasklist.adapters.CategoryAdapter
import com.odearmas.tasklist.data.entities.Category
import com.odearmas.tasklist.data.providers.CategoryDAO
import com.odearmas.tasklist.databinding.ActivityCategoriesBinding
import com.odearmas.tasklist.databinding.AddCategoryDialogBinding

class CategoriesActivity : AppCompatActivity() {

    lateinit var binding:ActivityCategoriesBinding
    lateinit var categoryDAO: CategoryDAO
    lateinit var categoryMutableList: MutableList<Category>
    lateinit var adapter: CategoryAdapter
    lateinit var addCategoryItem: MenuItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title=getString(R.string.categories_title)
        supportActionBar?.setBackgroundDrawable(getDrawable(R.color.menu))

        categoryDAO = CategoryDAO(this)
        categoryMutableList = categoryDAO.findAll().toMutableList()
        //Log.i("CATEGORIES", categoryMutableList.toString())
        initView()

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
        category8 = categoryDAO.insert(category8)*/



    }

    override fun onResume() {
        super.onResume()

        loadData()
    }

    private fun initView() {
        /*binding.addCategoryButton.setOnClickListener {
            addCategory()
        }

        adapter = CategoryAdapter(this, categoryMutableList, {
            onItemClickListener(it)
        }, {
            editCategory(it)
            return@CategoryAdapter true
        })*/
        adapter = CategoryAdapter(this,categoryMutableList)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadData(){
        categoryMutableList = categoryDAO.findAll().toMutableList()
        adapter.updateItems(categoryMutableList)
    }

private fun searchCategory(searchText: String) {
            try {
                categoryMutableList = categoryDAO.findCategoryByName(searchText).toMutableList()
                adapter.updateItems(categoryMutableList)
                // Log.i("HTTP", "${result.results}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.categories_menu,menu)
        val searchViewItem = menu.findItem(R.id.menu_search)
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

        //addCategoryItem = menu.findItem(R.id.new_category_button)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){

            R.id.new_category_button ->{
                //categoryDAO.insert(Category("Esto es una prueba"))
                addCategory()
                loadData()
                true
            }

            else -> {super.onOptionsItemSelected(item)}
        }
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
            val categoryName = binding.taskTextField.editText?.text.toString()
            if (categoryName.isNotEmpty()) {
                val category = Category(categoryName)
                categoryDAO.insert(category)
                loadData()
                Toast.makeText(this, R.string.add_category_success_message, Toast.LENGTH_LONG).show()
                alertDialog.dismiss()
            } else {
                binding.taskTextField.error = getString(R.string.add_category_empty_error)
            }
        }
    }

}