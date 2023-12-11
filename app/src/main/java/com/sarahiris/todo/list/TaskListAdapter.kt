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


class TaskListAdapter : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(MyItemsDiffCallback) {


    var onClickDelete: (Task) -> Unit = {}
    var onClickEdit: (Task) -> Unit = {}

    inner class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.taskTitle.text = task.title
            binding.taskDescription.text = task.description

            val deleteButton = binding.deletebutton

            deleteButton.setOnClickListener {
                onClickDelete(task)
            }

            val editButton = binding.editbutton

            editButton.setOnClickListener {
                onClickEdit(task)
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