package com.example.guru

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.guru.databinding.ActivityAddRecordBinding
import android.app.Activity
import android.content.Intent
import com.example.guru.AddRecordActivity.Companion.REQUEST_CALORIE
import com.example.guru.AddRecordActivity.Companion.REQUEST_EXERCISE_LIST


class AddRecordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddRecordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_record)
        binding = ActivityAddRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 운동 목록 선택 페이지로 이동
        binding.textExerciseList.setOnClickListener {
            val intent = Intent(this, ExerciseListActivity::class.java)
            startActivityForResult(intent, REQUEST_EXERCISE_LIST)
        }

        // 칼로리 입력 페이지로 이동
        binding.textCalorie.setOnClickListener {
            val intent = Intent(this, CalorieActivity::class.java)
            startActivityForResult(intent, REQUEST_CALORIE)
        }

        binding.btnSave.setOnClickListener {
            // 나중에 저장 로직 추가
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_EXERCISE_LIST -> {
                    val result = data.getStringExtra("exercise")
                    binding.textExerciseList.text = result ?: "운동 목록"
                }
                REQUEST_CALORIE -> {
                    val result = data.getStringExtra("calorie")
                    binding.textCalorie.text = result ?: "칼로리"
                }
            }
        }
    }

    companion object {
        const val REQUEST_EXERCISE_LIST = 1001
        const val REQUEST_CALORIE = 1002
    }
}
