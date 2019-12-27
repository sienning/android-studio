package com.example.td2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.td2.network.API
import kotlinx.coroutines.launch

class TasksViewModel : ViewModel() {
    val taskListLiveData = MutableLiveData<List<Task>?>()
    private val repository = TasksRepository()

    fun loadTasks() {
        viewModelScope.launch {
            val taskList = repository.loadTasks()
            taskListLiveData.postValue(taskList)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            val success = repository.removeTask(task)
            if(success) {
                val newList = taskListLiveData.value.orEmpty().toMutableList()
                newList.remove(task)
                taskListLiveData.postValue(newList)
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            val newTask = repository.createTask(task)
            val newList = taskListLiveData.value.orEmpty().toMutableList()

            if(newTask!= null) {
                newList.add(newTask)
                taskListLiveData.postValue(newList)
            }
        }
    }

    fun editTask(task: Task) {
        viewModelScope.launch {
            val newTask = repository.updateTask(task)
            if (newTask != null) {
                val newList = taskListLiveData.value.orEmpty().toMutableList()
                val position = newList.indexOfFirst { it.id == newTask.id }
                newList[position] = task
                taskListLiveData.postValue(newList)
            }
        }
    }
}
