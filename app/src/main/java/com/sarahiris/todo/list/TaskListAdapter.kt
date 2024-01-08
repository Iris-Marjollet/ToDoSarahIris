package com.sarahiris.todo.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sarahiris.todo.R
import com.sarahiris.todo.databinding.FragmentTaskListBinding
import com.sarahiris.todo.databinding.ItemTaskBinding
import java.util.UUID

object MyItemsDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) : Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task) : Boolean {
        return oldItem == newItem
    }
}

interface TaskListListener {
    fun onClickDelete(task: Task)
    fun onClickEdit(task: Task)
}

//HERE////////////////////
class TaskListAdapter(val listener: TaskListListener) : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(MyItemsDiffCallback) {


    inner class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.taskTitle.text = task.title
            binding.taskDescription.text = task.description

            val deleteButton = binding.deletebutton

            deleteButton.setOnClickListener {
                listener.onClickDelete(task)
            }

            val editButton = binding.editbutton

            editButton.setOnClickListener {
                listener.onClickEdit(task)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}