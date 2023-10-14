package com.example.fribyrunner

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class PedometerActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var stepCountSensor: Sensor? = null
    private lateinit var stepCountView: TextView
    private lateinit var resetButton: Button
    private var currentSteps = 0

    private lateinit var saveButton: Button
    private lateinit var stepsListView: ListView
    private val stepsList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

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

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        stepCountSensor?.let {
            Toast.makeText(this, "No Step Sensor", Toast.LENGTH_SHORT).show()
        }

        resetButton.setOnClickListener {
            currentSteps = 0
            stepCountView.text = currentSteps.toString()
        }

        // Adapter 설정 및 ListView에 연결
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stepsList)
        stepsListView.adapter = adapter

        saveButton.setOnClickListener {
            saveSteps()
        }
    }

    override fun onStart() {
        super.onStart()
        stepCountSensor?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0f) {
                currentSteps++
                stepCountView.text = currentSteps.toString()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do nothing for now
    }

    private fun saveSteps() {
        stepsList.add(0, "걸음수: $currentSteps")
        adapter.notifyDataSetChanged()
    }
}