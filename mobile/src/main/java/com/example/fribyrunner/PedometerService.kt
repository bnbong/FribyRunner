package com.example.fribyrunner

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class PedometerService : Service() , SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepCountSensor: Sensor? = null
    private var currentSteps = 0


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        stepCountSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
        }

        startForeground(FOREGROUND_SERVICE_TYPE_HEALTH, createNotification())
    }

    private fun createNotification(): Notification {
        val channelId = "pedometerNotificationChannel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Pedometer Service"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId).setOngoing(true)
            .build()
    }


    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0f) {
                currentSteps++
                updateSteps()
            }
        }
    }

    private fun updateSteps() {
        val intent = Intent("com.example.fribyrunner.UPDATE_STEPS")
        intent.putExtra("steps", currentSteps)
        sendBroadcast(intent)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do nothing for now
    }
}