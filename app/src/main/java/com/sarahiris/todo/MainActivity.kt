package com.sarahiris.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sarahiris.todo.data.Api
import com.sarahiris.todo.detail.DetailActivity
import com.sarahiris.todo.list.TaskListFragment
import com.sarahiris.todo.list.TasksListViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    val taskListFragment = TaskListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


}