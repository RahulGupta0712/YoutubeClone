package com.example.notyoutube

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notyoutube.databinding.ActivityVideoFullModeBinding
import com.example.notyoutube.databinding.ActivityVideoFullModeProfileBinding

class videoFullModeProfile : AppCompatActivity() {
    private val binding: ActivityVideoFullModeProfileBinding by lazy {
        ActivityVideoFullModeProfileBinding.inflate(layoutInflater)
    }
    private lateinit var dataList: ArrayList<descriptionVideo>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        dataList = dataVideos().getData()


        val position = intent.getIntExtra("", -1)   // getting the position in dataList
        if (position != -1) {
            binding.profile00.setImageResource(R.drawable.emoji)
            binding.title200.text = dataList.get(position).title
            binding.viewCount00.text = dataList.get(position).viewCount
            binding.timeAgo00.text = dataList.get(position).timeAgo
            binding.channelName00.text = "The Memes"
            binding.background00.setImageResource(dataList.get(position).thumbnail)
            binding.subsCount00.text = "6M"
            binding.likeCount00.text = dataList.get(position).likeCount
            binding.commentCount00.text = dataList.get(position).commentCount
        }
    }
}