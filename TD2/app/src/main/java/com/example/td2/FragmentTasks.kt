package com.example.td2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.td2.network.API.userService
import com.example.td2.network.UserService
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.*




class FragmentTasks : Fragment() {
    private val adapter = TasksAdapter()

    private val viewModel by lazy {
        ViewModelProvider(this).get(TasksViewModel::class.java)
    }

    // Création:
    private val coroutineScope = MainScope()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        imageView.setOnClickListener{
            val intent = Intent("android.intent.action.Customer")
            startActivity(intent)
        }

        floatingActionButton.setOnClickListener {
            val task = Task(id = UUID.randomUUID().toString(), title = "COUCOU")
            viewModel.addTask(task)
        }

        adapter.onDeleteClickListener = {
            viewModel.deleteTask(it)
        }

        viewModel.taskListLiveData.observe(this, Observer { newList ->
            adapter.list = newList.orEmpty()
        })
        tasks_recycler_view.adapter = adapter
    }



    override fun onResume() {
        super.onResume()
        val glide = Glide.with(this)
        lifecycleScope.launch {
            val userInfo = userService.getInfo()
            glide.load(userInfo).apply(RequestOptions.circleCropTransform()).into(imageView)
        }

        //Glide.with(this).load("https://goo.gl/gEgYUd").apply(RequestOptions.circleCropTransform()).into(imageView)
        viewModel.loadTasks()
    }


    override fun onDestroy() {
        super.onDestroy()
        // Suppression dans onDestroy():
        coroutineScope.cancel()
    }


}