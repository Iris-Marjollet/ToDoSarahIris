package com.sarahiris.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sarahiris.todo.data.Api
import com.sarahiris.todo.list.TaskListFragment
import com.sarahiris.todo.list.TasksListViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    val taskListFragment = TaskListFragment()

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

            val taskListFragment =
                supportFragmentManager.findFragmentByTag("TaskListFragmentTag") as? TaskListFragment

            taskListFragment?.let { fragment ->

                // Refresh the task list using the ViewModel
                fragment.viewModel.refresh()
            }
        }

    }
}