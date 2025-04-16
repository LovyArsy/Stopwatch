package com.smkth.aplikasistopwatch

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.smkth.aplikasistopwatch.databinding.ActivityStopwatchBinding
import org.json.JSONArray

class Stopwatch : AppCompatActivity() {
    private lateinit var binding: ActivityStopwatchBinding
    private var isRunning = false
    private var startTime = 0L
    private var elapsedTime = 0L

    private lateinit var sharedPreferences: SharedPreferences
    private val lapList = mutableListOf<String>()
    private val handler = Handler(Looper.getMainLooper())

    private val updateRunnable = object : Runnable {
        override fun run() {
            elapsedTime = SystemClock.elapsedRealtime() - startTime
            binding.tvTime.text = formatTime(elapsedTime)
            handler.postDelayed(this, 10)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStopwatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("LapTimes", Context.MODE_PRIVATE)

        binding.btnStopStart.setOnClickListener {
            if (!isRunning) {
                startTime = SystemClock.elapsedRealtime() - elapsedTime
                handler.post(updateRunnable)
                binding.btnStopStart.text = "Stop"
                isRunning = true
            } else {
                handler.removeCallbacks(updateRunnable)
                binding.btnStopStart.text = "Start"
                isRunning = false
            }
        }

        binding.btnLap.setOnClickListener {
            if (isRunning) {
                val lapTime = formatTime(elapsedTime)
                val lapText = "Lap ${lapList.size + 1}: $lapTime"
                lapList.add(lapText)
                addLapTime(lapText)
            }
        }

        binding.btnReload.setOnClickListener {
            if (lapList.isNotEmpty()) {
                val totalTime = formatTime(elapsedTime)
                val group = JSONArray()
                group.put(totalTime)
                lapList.forEach { group.put(it) }

                val dataArray = JSONArray(sharedPreferences.getString("lapGroups", "[]"))
                dataArray.put(group)
                sharedPreferences.edit().putString("lapGroups", dataArray.toString()).apply()
            }

            handler.removeCallbacks(updateRunnable)
            elapsedTime = 0L
            binding.tvTime.text = formatTime(elapsedTime)
            binding.btnStopStart.text = "Start"
            isRunning = false
            binding.llLapContainer.removeAllViews()
            lapList.clear()
        }

        binding.btnLihat.setOnClickListener {
            startActivity(Intent(this, Data_Waktu::class.java))
        }
    }

    private fun formatTime(ms: Long): String {
        val milliseconds = (ms % 1000) / 10
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60)) % 60
        val hours = ms / (1000 * 60 * 60)
        return String.format("%02d:%02d:%02d:%02d", hours, minutes, seconds, milliseconds)
    }

    private fun addLapTime(lapText: String) {
        val textView = TextView(this).apply {
            text = lapText
            textSize = 18f
        }
        binding.llLapContainer.addView(textView)
    }
}
