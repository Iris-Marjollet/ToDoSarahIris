package com.sarahiris.todo.list

import retrofit2.Response
import retrofit2.http.GET

interface TaskWebService {
    @GET("/rest/v2/tasks/")
    suspend fun fetchTasks(): Response<List<Task>>
}