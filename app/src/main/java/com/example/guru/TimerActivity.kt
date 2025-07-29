package com.example.guru

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.guru.R


class TimerActivity : AppCompatActivity() {
    // UI 요소 선언
    private lateinit var spinnerRoutines: Spinner
    private lateinit var tvTimer: TextView
    private lateinit var btnPlayPauseTimer: ImageButton
    private lateinit var tvCurrentSpeed: TextView
    private lateinit var btnStartWorkout: Button

    // 타이머 관련 변수
    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 0 // 남은 시간 (밀리초)
    private var isTimerRunning: Boolean = false // 타이머 실행 중 여부
    private var initialWorkoutDuration: Long = 0 // 초기 운동 시간 (밀리초)

    // 루틴 데이터 (임시 데이터, 실제로는 Firestore에서 불러올 예정)
    private val workoutRoutines = mutableListOf<WorkoutRoutine>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        // UI 요소 초기화
        spinnerRoutines = findViewById(R.id.spinner_routines)
        tvTimer = findViewById(R.id.tv_timer)
        btnPlayPauseTimer = findViewById(R.id.btn_play_pause_timer)
        tvCurrentSpeed = findViewById(R.id.tv_current_speed)
        btnStartWorkout = findViewById(R.id.btn_start_workout)

        // 임시 루틴 데이터 추가 (나중에 Firestore에서 불러오는 로직으로 대체)
        workoutRoutines.add(WorkoutRoutine("루틴 선택", 0, 0)) // 기본값
        workoutRoutines.add(WorkoutRoutine("루틴 1", 10, 6)) // 10분, 속도 6
        workoutRoutines.add(WorkoutRoutine("인터벌 루틴 1", 5, 8)) // 5분, 속도 8
        workoutRoutines.add(WorkoutRoutine("루틴 2", 15, 5)) // 15분, 속도 5

        // 스피너 어댑터 설정
        val routineNames = workoutRoutines.map { it.name }.toMutableList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, routineNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRoutines.adapter = adapter

        // 스피너 아이템 선택 리스너
        spinnerRoutines.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedRoutine = workoutRoutines[position]
                if (selectedRoutine.durationMinutes > 0) { // "루틴 선택"이 아닐 때만 적용
                    initialWorkoutDuration = selectedRoutine.durationMinutes * 60 * 1000L // 분을 밀리초로 변환
                    timeLeftInMillis = initialWorkoutDuration
                    tvCurrentSpeed.text = selectedRoutine.speed.toString()
                    updateCountDownText()
                    resetTimer() // 새로운 루틴 선택 시 타이머 초기화
                } else {
                    // "루틴 선택"일 경우 타이머를 00:00으로 초기화하고 속도도 기본값으로
                    initialWorkoutDuration = 0
                    timeLeftInMillis = 0
                    tvCurrentSpeed.text = "0" // 또는 비워두기
                    updateCountDownText()
                    resetTimer()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때 (필요시 구현)
            }
        }

        // 타이머 재생/일시정지 버튼 클릭 리스너
        btnPlayPauseTimer.setOnClickListener {
            if (initialWorkoutDuration == 0L && !isTimerRunning) {
                Toast.makeText(this, "먼저 루틴을 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isTimerRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        // 운동 시작 버튼 클릭 리스너 (실제 운동 시작 및 타이머 초기화)
        btnStartWorkout.setOnClickListener {
            if (initialWorkoutDuration == 0L) {
                Toast.makeText(this, "먼저 루틴을 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            resetTimer() // 타이머 초기화
            startTimer() // 타이머 시작
            Toast.makeText(this, "운동을 시작합니다!", Toast.LENGTH_SHORT).show()
        }

        // 초기 타이머 텍스트 업데이트
        updateCountDownText()
    }

    // 타이머 시작 함수
    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                isTimerRunning = false
                btnPlayPauseTimer.setImageResource(R.drawable.ic_play_arrow)
                tvTimer.text = "00 : 00"
                Toast.makeText(this@TimerActivity, "운동 완료!", Toast.LENGTH_LONG).show()
                // 운동 완료 후 추가 동작 (예: 다음 루틴으로 이동, 결과 화면 표시 등)
            }
        }.start()
        isTimerRunning = true
        btnPlayPauseTimer.setImageResource(R.drawable.ic_pause) // 일시정지 아이콘으로 변경
    }

    // 타이머 일시정지 함수
    private fun pauseTimer() {
        countDownTimer?.cancel()
        isTimerRunning = false
        btnPlayPauseTimer.setImageResource(R.drawable.ic_play_arrow) // 재생 아이콘으로 변경
    }

    // 타이머 초기화 함수
    private fun resetTimer() {
        pauseTimer() // 현재 타이머가 실행 중이면 중지
        timeLeftInMillis = initialWorkoutDuration // 초기 설정 시간으로 되돌림
        updateCountDownText() // 텍스트 업데이트
        btnPlayPauseTimer.setImageResource(R.drawable.ic_play_arrow) // 재생 아이콘으로 변경
    }

    // 타이머 텍스트 업데이트 함수
    private fun updateCountDownText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60

        val timeLeftFormatted = String.format("%02d : %02d", minutes, seconds)
        tvTimer.text = timeLeftFormatted
    }

    // 운동 루틴 데이터 클래스 (임시)
    data class WorkoutRoutine(
        val name: String,
        val durationMinutes: Int, // 운동 시간 (분)
        val speed: Int // 운동 속도
    )

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel() // 액티비티 종료 시 타이머도 반드시 중지
    }
}
