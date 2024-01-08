package com.sarahiris.todo.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarahiris.todo.data.Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class TasksListViewModel : ViewModel() {
    private val webService = Api.taskWebService

    public val tasksStateFlow = MutableStateFlow<List<Task>>(emptyList())



    fun refresh() {
        println("PLUS 1")
        viewModelScope.launch {

            println("PLUS 1.5")
            val response = webService.fetchTasks() // Call HTTP (opération longue)

            println("PLUS 2")
            if (!response.isSuccessful) { // à cette ligne, on a reçu la réponse de l'API
                Log.e("Network", "Error: ${response.message()}")
                return@launch
            }
            val fetchedTasks = response.body()!!

            println("PLUS 3")
            println(fetchedTasks.size)
            //val fetchedTasks = Api.taskWebService.fetchTasks().body()!!
            tasksStateFlow.value =
                fetchedTasks // on modifie le flow, ce qui déclenche ses observers
        }
    }

    fun add(task: Task) {
        viewModelScope.launch {
            val response = webService.create(task)
            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                return@launch
            }

            val createdTask = response.body()!!
            tasksStateFlow.value = tasksStateFlow.value + createdTask

        }
    }

    fun update(task: Task) {
        viewModelScope.launch {
            val response = webService.update(task)
            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                return@launch
            }

            val updatedTask = response.body()!!

            val updatedList = tasksStateFlow.value.map{
                if (it.id == updatedTask.id) updatedTask else it
            }
            tasksStateFlow.value = updatedList

        }
    }

    fun remove(task: Task) {
        viewModelScope.launch {
            val response = webService.delete(task.id)
            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                return@launch
            }

            //val deletedTask = response.body()!!
            tasksStateFlow.value = tasksStateFlow.value - task

        }
    }
    // à compléter plus tard:
    //fun add(task: Task) {}
    //fun edit(task: Task) {}
    //fun remove(task: Task) {}
}