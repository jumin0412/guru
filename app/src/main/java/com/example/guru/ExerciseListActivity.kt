package com.example.guru

import android.os.Bundle
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ExerciseListActivity : AppCompatActivity() {

    private lateinit var editExerciseName: EditText
    private lateinit var spinnerAltitude: Spinner
    private lateinit var spinnerSpeed: Spinner
    private lateinit var btnAdd: Button

    // 더블클릭 감지용 시간 변수
    private var lastAltitudeClickTime = 0L
    private var lastSpeedClickTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list)

        editExerciseName = findViewById(R.id.editExerciseName)
        spinnerAltitude = findViewById(R.id.spinnerAltitude)
        spinnerSpeed = findViewById(R.id.spinnerSpeed)
        btnAdd = findViewById(R.id.btnAdd)

        // 드롭다운 데이터 리스트
        val altitudeList = mutableListOf("0", "0.5", "1.0", "1.5", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0", "8.0", "9.0", "10.0")
        val speedList = mutableListOf("1", "3", "6", "9", "10")

        // 어댑터 설정
        val altitudeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, altitudeList)
        spinnerAltitude.adapter = altitudeAdapter

        val speedAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, speedList)
        spinnerSpeed.adapter = speedAdapter

        // 고도 스피너 터치 리스너
        spinnerAltitude.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastAltitudeClickTime < 500) {
                    showInputDialog("고도") { value ->
                        altitudeList.add(0, value)
                        spinnerAltitude.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, altitudeList)
                        spinnerAltitude.setSelection(0)
                    }
                    lastAltitudeClickTime = 0
                    true
                } else {
                    lastAltitudeClickTime = currentTime
                    false
                }
            } else {
                false
            }
        }

        // 속도 스피너 터치 리스너
        spinnerSpeed.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastSpeedClickTime < 500) {
                    showInputDialog("속도") { value ->
                        speedList.add(0, value)
                        spinnerSpeed.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, speedList)
                        spinnerSpeed.setSelection(0)
                    }
                    lastSpeedClickTime = 0
                    true
                } else {
                    lastSpeedClickTime = currentTime
                    false
                }
            } else {
                false
            }
        }

        // 추가 버튼 클릭 이벤트
        btnAdd.setOnClickListener {
            val name = editExerciseName.text.toString()
            val altitude = spinnerAltitude.selectedItem.toString()
            val speed = spinnerSpeed.selectedItem.toString()

            Toast.makeText(this, "운동 추가: $name / 고도: $altitude / 속도: $speed", Toast.LENGTH_SHORT).show()
            // DB 저장 또는 다음 화면 이동 로직 추가
        }
    }

    // 텍스트 입력 다이얼로그 함수
    private fun showInputDialog(title: String, onValueEntered: (String) -> Unit) {
        val editText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("$title 값 입력")
            .setView(editText)
            .setPositiveButton("확인") { _, _ ->
                val input = editText.text.toString()
                if (input.isNotEmpty()) {
                    onValueEntered(input)
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }
}