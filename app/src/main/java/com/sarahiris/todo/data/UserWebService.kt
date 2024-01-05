package com.sarahiris.todo.data

import androidx.lifecycle.LifecycleObserver
import retrofit2.Response
import retrofit2.http.GET

interface UserWebService : LifecycleObserver {

    @GET("/sync/v9/user/")
    suspend fun fetchUser(): Response<User>


}