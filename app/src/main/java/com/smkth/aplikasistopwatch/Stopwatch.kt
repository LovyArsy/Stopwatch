package com.smkth.aplikasistopwatch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.smkth.aplikasistopwatch.databinding.ActivityStopwatchBinding

class Stopwatch : AppCompatActivity() {

    private lateinit var binding: ActivityStopwatchBinding
    private var isRunning = false
    private var startTime = 0L
    private var elapsedTime = 0L

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

        // Tombol Start/Stop
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

        // Tombol Reset (Menghapus semua lap juga)
        binding.btnReload.setOnClickListener {
            if (elapsedTime > 0) {
                addLapTime(formatTime(elapsedTime))
            }
            handler.removeCallbacks(updateRunnable)
            elapsedTime = 0L
            binding.tvTime.text = formatTime(elapsedTime)
            binding.btnStopStart.text = "Start"
            isRunning = false
            binding.llLapContainer.removeAllViews() // Menghapus semua lap sebelumnya
        }

        // Tombol Lap
        binding.btnLap.setOnClickListener {
            if (isRunning) {
                addLapTime(formatTime(elapsedTime))
            }
        }
    }

    private fun formatTime(ms: Long): String {
        val milliseconds = (ms % 1000) / 10
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60)) % 60
        val hours = ms / (1000 * 60 * 60)
        return String.format("%02d:%02d:%02d:%02d", hours, minutes, seconds, milliseconds)
    }

    private fun addLapTime(lapTime: String) {
        val textView = TextView(this).apply {
            text = "Lap ${binding.llLapContainer.childCount + 1}: $lapTime"
            textSize = 18f
        }
        binding.llLapContainer.addView(textView)
    }
}
