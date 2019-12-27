package com.example.td2

import com.example.td2.network.API

class TasksRepository {
    private val tasksService = API.tasksService

    suspend fun loadTasks(): List<Task>? {
        val tasksResponse = tasksService.getTasks()
        return if (tasksResponse.isSuccessful) tasksResponse.body() else null
    }

    suspend fun removeTask(task: Task): Boolean {
        val response = tasksService.deleteTask(task.id)
        return response.isSuccessful
    }

    suspend fun createTask(task: Task): Task? {
        val response = tasksService.createTask(task)
        return if (response.isSuccessful) response.body() else null
    }
    suspend fun updateTask(task: Task): Task?  {
        val response = tasksService.updateTask(task)
        return if (response.isSuccessful) response.body() else null
    }
}