package com.example.guru

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.*

class CalorieActivity : AppCompatActivity() {
    private lateinit var spinnerExerciseType: Spinner
    private lateinit var editTime: EditText
    private lateinit var txtResult: TextView
    private lateinit var btnCalculate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calorie)
        spinnerExerciseType = findViewById(R.id.spinnerExerciseType)
        editTime = findViewById(R.id.editTime)
        txtResult = findViewById(R.id.txtResult)
        btnCalculate = findViewById(R.id.btnCalculate)

        val exerciseList = listOf("해당 없음", "걷기", "달리기", "자전거")
        val caloriesPerMinute = mapOf("걷기" to 4.0, "달리기" to 10.0, "자전거" to 8.0)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, exerciseList)
        spinnerExerciseType.adapter = adapter

        btnCalculate.setOnClickListener {
            val type = spinnerExerciseType.selectedItem.toString()
            val time = editTime.text.toString().toIntOrNull()

            if (type == "해당 없음" || time == null) {
                Toast.makeText(this, "운동 유형과 시간을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val calorie = caloriesPerMinute[type]?.times(time) ?: 0.0
            txtResult.text = "칼로리: %.2f kcal".format(calorie)
        }
    }
}