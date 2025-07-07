package com.example.ch7_layout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ch7_layout.R.layout.activity_main

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // activity_main.xml을 화면에 출력
        setContentView(activity_main)

    }
}