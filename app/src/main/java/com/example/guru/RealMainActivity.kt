package com.example.guru

import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.guru.databinding.ActivityRealMainBinding


class RealMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRealMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRealMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 캘린더 날짜 변경 시 월 업데이트
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            binding.textMonth.text = "${month + 1}월"
        }

        // 버튼 클릭 리스너
        binding.btnTimer.setOnClickListener {
            startActivity(Intent(this, TimerActivity::class.java))
        }

        binding.btnAnalyze.setOnClickListener {
            startActivity(Intent(this, AnalysisActivity::class.java))
        }

        binding.btnAddExercise.setOnClickListener {
            startActivity(Intent(this, AddRecordActivity::class.java))
        }
    }
}