package com.example.fribyrunner

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat

class PedometerActivity : AppCompatActivity() {
    private lateinit var stepCountView: TextView
    private lateinit var resetButton: Button
    private lateinit var saveButton: Button
    private lateinit var stepsListView: ListView
    private val stepsList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>
    private var currentSteps = 0

    private val stepUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            currentSteps = intent.getIntExtra("steps", 0)
            stepCountView.text = currentSteps.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedometer)

        stepCountView = findViewById(R.id.stepCountView)
        resetButton = findViewById(R.id.resetButton)
        saveButton = findViewById(R.id.saveButton)
        stepsListView = findViewById(R.id.stepsListView)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 0)
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 0)
        }

        // Adapter 설정 및 ListView에 연결
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stepsList)
        stepsListView.adapter = adapter

        resetButton.setOnClickListener {
            currentSteps = 0
            stepCountView.text = currentSteps.toString()
        }

        saveButton.setOnClickListener {
            saveSteps()
        }
    }

    override fun onStart() {
        super.onStart()

        val intent = Intent(this, PedometerService::class.java)
        ContextCompat.startForegroundService(this, intent)

        val filter = IntentFilter()
        filter.addAction("com.example.fribyrunner.UPDATE_STEPS")
        registerReceiver(stepUpdateReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(stepUpdateReceiver)
    }

    private fun saveSteps() {
        stepsList.add(0, "걸음수: $currentSteps")
        adapter.notifyDataSetChanged()
    }

}