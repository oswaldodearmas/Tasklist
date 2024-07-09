package com.odearmas.tasklist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.odearmas.tasklist.data.entities.Category
import com.odearmas.tasklist.data.entities.Task
import com.odearmas.tasklist.data.providers.CategoryDAO
import com.odearmas.tasklist.data.providers.TaskDAO
import com.odearmas.tasklist.databinding.ItemCategoryBinding
import com.odearmas.tasklist.databinding.ItemTaskBinding

class TaskAdapter(
    context: Context,
    private var items:List<Task> = listOf(),
    private val onTaskClickListener: (position:Int) -> Unit,
    private val onEditClickListener: (position:Int) -> Unit,
    private val onDeleteClickListener: (position:Int) -> Unit,
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.render(items[position])
        holder.itemView.setOnClickListener { onTaskClickListener(position) }
        holder.binding.taskEditButton.setOnClickListener { onEditClickListener(position) }
        holder.binding.taskDeleteButton.setOnClickListener { onDeleteClickListener(position) }
        //holder.itemView.setOnLongClickListener { onLongClickListener(position) }
    }

    fun updateItems(results: List<Task>) {
        items = results
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun render(task: Task) {
            binding.nameTaskTextView.text = task.name
            //val realId = categoryDAO.findCategoryByName(category.categoryName)
            //binding.numberOfTasksTextView.text = taskDAO.countTasksInCategory(category.categoryId)
            //binding.colorView.setBackgroundColor(category.color.toColorInt())
        }
    }
}