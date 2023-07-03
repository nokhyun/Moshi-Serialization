package com.nokhyun.parseexam

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

/** 알림권한 체크기능 안넣음 */
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            runApi34Job()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun runApi34Job() {
        // JOB
        val networkRequestBuilder = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .addCapability(NET_CAPABILITY_VALIDATED)

        val jobInfo = JobInfo.Builder(1001, ComponentName(this@MainActivity, ParseExamJobService::class.java))
            .setUserInitiated(true)
//            .setDataTransfer(true)  // TODO ??
            .setRequiredNetwork(networkRequestBuilder.build())
            .setEstimatedNetworkBytes( 1024 * 1024, 1024 * 1024)
            .build()

        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobInfo)
    }

    private fun log(msg: String) {
        Log.e(this.javaClass.simpleName, msg)
    }
}