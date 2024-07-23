package com.example.notyoutube

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notyoutube.databinding.ActivityVideoFullModeBinding
import com.github.ybq.android.spinkit.style.FadingCircle
import com.github.ybq.android.spinkit.style.Wave

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

        // remove the import statement of R, then do the following
        val progressBar = findViewById<View>(R.id.spin_kit) as ProgressBar
        progressBar.indeterminateDrawable = FadingCircle()

        val prof = intent.getIntExtra("profile", -1)
        val thumb = intent.getIntExtra("thumbnail", 0)

        val titl = intent.getStringExtra("title")
        val views = intent.getStringExtra("viewsCount")
        val tim = intent.getStringExtra("time")
        val timago = intent.getStringExtra("timeAgo")
        val channel = intent.getStringExtra("channelName")

        if (prof != -1) {
            binding.profile.setImageResource(prof)
            binding.title2.text = titl
            binding.viewCount.text = views
            binding.timeAgo.text = timago
            binding.channelName.text = channel
            binding.background.setImageResource(thumb)
            binding.subsCount.text = "6M"
        }

    }
}