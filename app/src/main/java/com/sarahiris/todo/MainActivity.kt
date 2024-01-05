package com.sarahiris.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.sarahiris.todo.data.Api
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun onResume() {
        println("IN RESUME")
        super.onResume()
        lifecycleScope.launch {

            println("asynch")
            val user = Api.userWebService.fetchUser().body()!!

            val userTextView = findViewById<TextView>(R.id.headerText)
            userTextView.text = user.name

        }




    }
}