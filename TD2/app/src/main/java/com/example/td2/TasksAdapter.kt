package com.example.td2

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.item_task.view.*
import kotlin.properties.Delegates


class TasksAdapter : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    var list: List<Task> by Delegates.observable(emptyList()) {
            _, _, _ -> notifyDataSetChanged()
    }
    var onDeleteClickListener: (Task) -> Unit = {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView = itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task, position: Int) {
            itemView.task_title.text = task.title
            itemView.task_desc.text = task.desc
            itemView.delete_button.setOnClickListener {
                onDeleteClickListener.invoke(list[position])
            }
        }
    }
}