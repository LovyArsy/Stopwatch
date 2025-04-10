package com.smkth.aplikasistopwatch
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.smkth.aplikasistopwatch.databinding.ActivityDataWaktuBinding
import com.smkth.aplikasistopwatch.databinding.ActivityStopwatchBinding

class Data_Waktu : AppCompatActivity() {
    private lateinit var binding: ActivityDataWaktuBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var lapTimes: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataWaktuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("LapTimes", Context.MODE_PRIVATE)
        lapTimes = loadLapTimes()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lapTimes)
        binding.lapTimesList.adapter = adapter

        binding.backButton.setOnClickListener {
            val intent = Intent(this, Stopwatch::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun loadLapTimes(): MutableList<String> {
        val savedLapTimes = sharedPreferences.getStringSet("LapTimes", emptySet())
        return savedLapTimes?.toMutableList() ?: mutableListOf()
    }
}
