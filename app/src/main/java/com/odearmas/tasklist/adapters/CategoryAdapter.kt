package com.odearmas.tasklist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.odearmas.tasklist.data.entities.Category
import com.odearmas.tasklist.data.providers.TaskDAO
import com.odearmas.tasklist.databinding.ItemCategoryBinding

class CategoryAdapter(
    context: Context,
    private var items:List<Category> = listOf(),
    private val onCategoryClickListener: (position:Int) -> Unit,
    private val onEditClickListener: (position:Int) -> Unit,
    private val onDeleteClickListener: (position:Int) -> Unit,
    // private val onLongClickListener: (position:Int) -> Boolean
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var taskDAO: TaskDAO = TaskDAO(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.render(items[position])
        holder.itemView.setOnClickListener { onCategoryClickListener(position) }
        holder.binding.categoryEditButton.setOnClickListener { onEditClickListener(position) }
        holder.binding.categoryDeleteButton.setOnClickListener { onDeleteClickListener(position) }
        //holder.itemView.setOnLongClickListener { onLongClickListener(position) }
    }

    fun updateItems(results: List<Category>) {
        items = results
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun render(category: Category) {
            binding.nameTextView.text = category.categoryName
            //binding.numberOfTasksTextView.text = taskDAO.countByCategoryAndDone(category).toString()
            //binding.colorView.setBackgroundColor(category.color.toColorInt())
        }
    }
}