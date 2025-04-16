package com.smkth.aplikasistopwatch

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.smkth.aplikasistopwatch.databinding.ActivityDataWaktuBinding
import org.json.JSONArray

class Data_Waktu : AppCompatActivity() {

    private lateinit var binding: ActivityDataWaktuBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val groupList = mutableListOf<JSONArray>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataWaktuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("LapTimes", MODE_PRIVATE)
        val dataArray = JSONArray(sharedPreferences.getString("lapGroups", "[]"))

        val titles = ArrayList<String>()
        for (i in 0 until dataArray.length()) {
            val group = dataArray.getJSONArray(i)
            titles.add("Data Waktu ${group.getString(0)}")
            groupList.add(group)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, titles)
        binding.lapTimesList.adapter = adapter

        binding.lapTimesList.setOnItemClickListener { _, _, position, _ ->
            val group = groupList[position]
            val lapDetails = ArrayList<String>()
            for (j in 1 until group.length()) {
                lapDetails.add(group.getString(j))
            }

            val fragment = DetailLapFragment.newInstance(lapDetails)
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.backButton.setOnClickListener {
            startActivity(Intent(this, Stopwatch::class.java))
            finish()
        }
    }
}
