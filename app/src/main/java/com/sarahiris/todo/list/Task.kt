package com.sarahiris.todo.list

data class Task(val id: String, val title: String, val description: String = "Description par défaut") : java.io.Serializable

