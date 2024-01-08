package com.sarahiris.todo.data

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import androidx.lifecycle.*
import com.sarahiris.todo.R
import com.sarahiris.todo.list.TaskWebService
import kotlinx.coroutines.launch


object Api : AppCompatActivity(){
    private const val TOKEN = "d016dcdac97c11bb1a0476c904a79504f914cdc5"

    val userWebService : UserWebService by lazy {

        println("lazy web service")
        retrofit.create(UserWebService::class.java)

    }

    val taskWebService : TaskWebService by lazy {

        println("lazy task web service")
        retrofit.create(TaskWebService::class.java)

    }


    private val retrofit by lazy {
        // client HTTP

        println("HTTP CLIENT")
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $TOKEN")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        // transforme le JSON en objets kotlin et inversement
        val jsonSerializer = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }



        // instance retrofit pour  les webServimplémenterices:
        Retrofit.Builder()
            .baseUrl("https://api.todoist.com/")
            .client(okHttpClient)
            .addConverterFactory(jsonSerializer.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}