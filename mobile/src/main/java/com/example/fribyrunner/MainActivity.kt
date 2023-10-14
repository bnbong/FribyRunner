package com.example.fribyrunner

import android.content.Context
import android.Manifest
import android.content.Intent
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

class MainActivity : AppCompatActivity(){

    private lateinit var startPedometerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Pedometer 시작 버튼 초기화 및 클릭 리스너 설정
        startPedometerButton = findViewById(R.id.startPedometerButton)
        startPedometerButton.setOnClickListener {
            val intent = Intent(this@MainActivity, PedometerActivity::class.java)
            startActivity(intent)
        }
    }
}