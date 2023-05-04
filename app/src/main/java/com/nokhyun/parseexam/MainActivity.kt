package com.nokhyun.parseexam

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private val btn: Button by lazy { findViewById(R.id.btnFetchData) }
    private val btnFetchSerialization: Button by lazy { findViewById(R.id.btnFetchSerialization) }
    private val tvResult: TextView by lazy { findViewById(R.id.tvResult) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
            mainViewModel.event(MainViewModelContract.TodoEvent.FetchTodo)
        }
        btnFetchSerialization.setOnClickListener {
            mainViewModel.event(MainViewModelContract.TodoEvent.FetchTodoSerialization)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.state.filter { it.result != null }.collectLatest {
                    tvResult.text = it.result.toString()
                }
            }
        }
    }

    private fun log(msg: String) {
        Log.e(this.javaClass.simpleName, msg)
    }
}