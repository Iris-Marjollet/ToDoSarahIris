package com.sarahiris.todo.list

import android.content.Intent
import android.os.Bundle
import android.system.Os.remove
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.sarahiris.todo.R
import com.sarahiris.todo.data.Api
import com.sarahiris.todo.data.Api.findViewById
import com.sarahiris.todo.data.User
import com.sarahiris.todo.databinding.FragmentTaskListBinding
import com.sarahiris.todo.detail.DetailActivity
import kotlinx.coroutines.launch
import java.util.UUID

class TaskListFragment : Fragment() {

    val viewModel: TasksListViewModel by viewModels()



    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )


    fun setTaskList(task: List<Task>){
        taskList = task

    }

    private val adapterListener : TaskListListener = object : TaskListListener {
        override fun onClickDelete(task: Task) {
            taskList = taskList - task
            refreshAdapter()}
        override fun onClickEdit(task: Task) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("task", task)
            editTask.launch(intent)}
    }
    private val adapter =  TaskListAdapter(adapterListener)
    private lateinit var binding : FragmentTaskListBinding

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("taskList", ArrayList(taskList))
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskListBinding.inflate(inflater, container, false)
        val rootView =  binding.root
        //val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        adapter.submitList(taskList)

        val rootView2 = inflater.inflate(R.layout.fragment_task_list, container, false)

        val userTextView = rootView2.findViewById<TextView>(R.id.headerText)



        return rootView
    }

    private val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultTask = result.data?.getSerializableExtra("task") as Task?
        if(resultTask != null) {
            taskList += resultTask
            refreshAdapter()
        }
    }

    private val editTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultTask = result.data?.getSerializableExtra("task") as Task?
        if(resultTask != null) {
            taskList = taskList.map { if (it.id == resultTask.id) resultTask else it }
            refreshAdapter()
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
                taskList = savedTaskList
                refreshAdapter()
            }
        }


        // Abonnez le fragment aux changements du StateFlow du ViewModel
        lifecycleScope.launch {
            viewModel.tasksStateFlow.collect { newList ->
                // Mettez à jour la liste dans l'adapter avec la nouvelle liste du StateFlow
                taskList = newList
                //taskList = Api.taskWebService.fetchTasks().body()!!
                refreshAdapter()
            }
        }


    }

    override fun onResume() {
        println("IN RESUME")
        super.onResume()

        lifecycleScope.launch {

            println("asynch")
            val user = Api.userWebService.fetchUser().body()!!

            binding?.headerText?.text = user.name
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
        adapter.notifyDataSetChanged()
    }

}

