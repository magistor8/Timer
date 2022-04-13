package com.magistor8.timer.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.magistor8.timer.R
import com.magistor8.timer.databinding.ActivityMainBinding
import com.magistor8.timer.domain.MainContract
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val ids = mapOf(1 to R.id.layout1, 2 to R.id.layout2, 3 to R.id.layout3)
    private lateinit var binding: ActivityMainBinding
    private var job: Job? = null
    private val viewModel : MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()
        startCoroutine()
    }

    private fun startCoroutine() {
        val scope = CoroutineScope(
            Dispatchers.Main
                    + SupervisorJob()
        )
        job = scope.launch {
            viewModel.viewState.collect { time -> updateTimer(time) }
        }
    }

    private fun setListener() {

        binding.newLayout.setOnClickListener {
            if (binding.layout2.root.visibility == View.GONE) {
                binding.layout2.root.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if (binding.layout3.root.visibility == View.GONE) {
                binding.layout3.root.visibility = View.VISIBLE
            }
        }

        for (i in 1..3) {
            binding.frame.findViewById<ConstraintLayout>(ids[i]!!).findViewById<AppCompatButton>(R.id.start).setOnClickListener {
                viewModel.onEvent(MainContract.Event.Start(i))
            }
            binding.frame.findViewById<ConstraintLayout>(ids[i]!!).findViewById<AppCompatButton>(R.id.pause).setOnClickListener {
                viewModel.onEvent(MainContract.Event.Pause(i))
            }
            binding.frame.findViewById<ConstraintLayout>(ids[i]!!).findViewById<AppCompatButton>(R.id.stop).setOnClickListener {
                viewModel.onEvent(MainContract.Event.Stop(i))
            }
        }
    }

    private fun updateTimer(time: Map<Int, String>) {
        for (i in 1..3) {
            binding.frame.findViewById<ConstraintLayout>(ids[i]!!).findViewById<TextView>(R.id.timer).text = time[i]
        }
    }

    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }
}