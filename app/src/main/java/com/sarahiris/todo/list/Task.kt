package com.sarahiris.todo.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Task(

    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String = "Description par défaut"
) : java.io.Serializable


