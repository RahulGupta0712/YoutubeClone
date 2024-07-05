package com.example.notyoutube

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notyoutube.databinding.ActivityVideoFullModeBinding

class videoFullMode : AppCompatActivity() {
    private val binding: ActivityVideoFullModeBinding by lazy {
        ActivityVideoFullModeBinding.inflate(layoutInflater)
    }
    private lateinit var dataList: ArrayList<data_model_home_video>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dataList = dataStorage().getData()
        val position = intent.getIntExtra("", -1)   // getting the position in dataList
        if (position != -1) {
            binding.profile.setImageResource(dataList.get(position).profile)
            binding.title2.text = dataList.get(position).title
            binding.viewCount.text = dataList.get(position).viewsCount
            binding.timeAgo.text = dataList.get(position).timeAgo
            binding.channelName.text = dataList.get(position).channelName
            binding.background.setImageResource(dataList.get(position).thumbnail)
            binding.subsCount.text = "6M"
        }

    }
}