package com.sarahiris.todo.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sarahiris.todo.R
import com.sarahiris.todo.data.Api
import com.sarahiris.todo.databinding.FragmentTaskListBinding
import com.sarahiris.todo.detail.DetailActivity
import kotlinx.coroutines.launch
import coil.load
import com.sarahiris.todo.user.UserActivity

class TaskListFragment : Fragment() {

    val viewModel: TasksListViewModel by viewModels()




    var taskList = emptyList<Task>()



    private val adapterListener : TaskListListener = object : TaskListListener {
        override fun onClickDelete(task: Task) {
            viewModel.remove(task)
            refreshAdapter()}
        override fun onClickEdit(task: Task) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("task", task)
            editTask.launch(intent)}
    }
    private val adapter =  TaskListAdapter(adapterListener)
    private lateinit var binding : FragmentTaskListBinding

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskListBinding.inflate(inflater, container, false)
        val rootView =  binding.root
        return rootView
    }

    private val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultTask = result.data?.getSerializableExtra("task") as Task?
        if(resultTask != null) {
            viewModel.add(resultTask)
            //refreshAdapter()
        }
    }

    private val editTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultTask = result.data?.getSerializableExtra("task") as Task?
        if(resultTask != null) {
            viewModel.update(resultTask)
            //refreshAdapter()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = binding.recycler
        recyclerView.adapter = adapter



        val addButton = binding.addbutton

        addButton.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            createTask.launch(intent)
        }

        if (savedInstanceState != null) {
            val savedTaskList = savedInstanceState.getSerializable("taskList") as? ArrayList<Task>
            if (savedTaskList != null) {
                //refreshAdapter()
            }
        }


        // Abonnez le fragment aux changements du StateFlow du ViewModel
        lifecycleScope.launch {
            viewModel.tasksStateFlow.collect { newList ->
                // Mettez à jour la liste dans l'adapter avec la nouvelle liste du StateFlow
                taskList = newList
                refreshAdapter()
            }
        }


        binding.avatarImage.setOnClickListener {
            val intent = Intent(activity, UserActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onResume() {
        println("IN RESUME")
        super.onResume()

        lifecycleScope.launch {

            println("asynch")
            val user = Api.userWebService.fetchUser().body()!!

            binding?.headerText?.text = user.name

            binding?.avatarImage?.load(user.avatar) {
                error(R.drawable.ic_launcher_background) //image par défault en cas d'erreur
            } //image

            //val userTextView = findViewById<TextView>(R.id.headerText)
            //userTextView.text = user.name

            //val list = Api.taskWebService.fetchTasks().body()!!


            /*val taskListFragment =
                supportFragmentManager.findFragmentByTag("TaskListFragmentTag") as? TaskListFragment
            println("BEFORE")
            taskListFragment?.let { fragment ->

                // Refresh the task list using the ViewModel
                println("REFRESH")
                fragment.viewModel.refresh()
            }*/

            println("REFRESH")
            viewModel.refresh()
        }



    }



    private fun refreshAdapter() {
        adapter.submitList(taskList)
    }

}

