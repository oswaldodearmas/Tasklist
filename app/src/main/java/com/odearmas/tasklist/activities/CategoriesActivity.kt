package com.odearmas.tasklist.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.odearmas.tasklist.R
import com.odearmas.tasklist.data.Category
import com.odearmas.tasklist.data.CategoryDAO
import com.odearmas.tasklist.data.Task
import com.odearmas.tasklist.data.TaskDAO
import com.odearmas.tasklist.databinding.ActivityCategoriesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriesActivity : AppCompatActivity() {

    lateinit var binding:ActivityCategoriesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    private fun searchByName(query: String) {
        // Llamada en segundo plano
        /*CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiService = RetrofitProvider.getRetrofit().create(HeroAPIService::class.java)
                val result = apiService.searchHeroesByName(query)
                if (result.response == "success") {
                    runOnUiThread {
                        heroListResponse = result
                        adapter.updateData(result.results)
                    }
                } else {
                    runOnUiThread {
                        adapter.updateData(emptyList())
                    }
                }
                // Log.i("HTTP", "${result.results}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }*/
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.categories_menu,menu)
        val searchViewItem = menu.findItem(R.id.menu_search)
        val searchView = searchViewItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(newText: String?): Boolean {

                searchViewItem.collapseActionView()
                if (newText != null) {
                    searchByName(newText)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchByName(newText)
                }
                return true
            }

        })
        return true
    }
}