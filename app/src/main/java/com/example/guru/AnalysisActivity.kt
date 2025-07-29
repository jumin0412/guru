package com.example.guru

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.tabs.TabLayout

class AnalysisActivity : AppCompatActivity() {
    private lateinit var barChart: BarChart
    private lateinit var tabLayout: TabLayout
    private lateinit var txtAverage: TextView
    private lateinit var txtMax: TextView
    private lateinit var txtTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)

        barChart = findViewById(R.id.barChart)
        tabLayout = findViewById(R.id.tabLayout)
        txtAverage = findViewById(R.id.txtAverage)
        txtMax = findViewById(R.id.txtMax)
        txtTotal = findViewById(R.id.txtTotal)
        setupChart()

        // 기본 7일 보기
        loadStatsForDays(7)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> loadStatsForDays(7)
                    1 -> loadStatsForDays(30)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupChart() {
        barChart.description.isEnabled = false
        barChart.setDrawGridBackground(false)
        barChart.setFitBars(true)
        barChart.axisRight.isEnabled = false
        barChart.axisLeft.axisMinimum = 0f
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
    }

    private fun loadStatsForDays(days: Int) {
        // 예시 데이터 - DB에서 실제로 가져와야 함
        val sampleData = listOf(30f, 40f, 20f, 50f, 60f, 45f, 70f) // 운동 시간 (분)
        val labels = listOf("일", "월", "화", "수", "목", "금", "토")

        val entries = sampleData.mapIndexed { index, value ->
            BarEntry(index.toFloat(), value)
        }

        val dataSet = BarDataSet(entries, "운동 시간")
        dataSet.color = Color.BLUE
        val data = BarData(dataSet)
        barChart.data = data
        barChart.invalidate()

        val average = sampleData.average()
        val max = sampleData.maxOrNull() ?: 0f
        val total = sampleData.sum()

        txtAverage.text = "평균 시간: %.1f분".format(average)
        txtMax.text = "최고 시간: %.1f분".format(max)
        txtTotal.text = "누적 시간: %.1f분".format(total)
    }
}
