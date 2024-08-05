package com.example.notyoutube

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.notyoutube.databinding.ActivityVideoFullModeBinding
import com.example.notyoutube.databinding.ActivityVideoFullModeProfileBinding
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Circle
import com.github.ybq.android.spinkit.style.Wave
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.squareup.picasso.Picasso

class videoFullModeProfile : AppCompatActivity() {
    private val binding: ActivityVideoFullModeProfileBinding by lazy {
        ActivityVideoFullModeProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val progressBar =
            findViewById<View>(R.id.spin_kit) as ProgressBar    // spin_kit is id of the loader (you can customise it)
        progressBar.indeterminateDrawable = Circle()

        val auth = FirebaseAuth.getInstance()
        val databaseRef = FirebaseDatabase.getInstance().reference

        val data =
            intent.getParcelableExtra<DataModelVideoDetails>("video")   // getting the position in dataList
        val mc = MediaController(this@videoFullModeProfile)
        mc.setAnchorView(binding.videoView)
        if (data != null) {
            val user = auth.currentUser
            if (user != null) {
                databaseRef.child("users").child(user.uid).child("Channel Name")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val channelName = snapshot.getValue<String>()
                            channelName?.let {
                                // got the channel name, now time for profile picture
                                databaseRef.child("users").child(user.uid).child("Profile Picture")
                                    .addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            val profilePicUri = snapshot.getValue<String>()
                                            profilePicUri?.let {
                                                // got the channel details, now show video
                                                Picasso.get().load(profilePicUri)
                                                    .into(binding.profile00)
                                                binding.title200.text = data.title
                                                binding.viewCount00.text = "0"
                                                val time =
                                                    (System.currentTimeMillis()-data.timePosted)/1000
                                                val minutes = time / 60
                                                val hour = minutes / 60
                                                val days = hour / 24
                                                val month = days / 30
                                                val year = days / 365

                                                val show=
                                                    if (year > 0) "$year year(s)"
                                                    else if (month > 0) "$month month(s)"
                                                    else if (days > 0) "$days day(s)"
                                                    else if (hour > 0) "$hour hr"
                                                    else if (minutes > 0) "$minutes min"
                                                    else "$time sec"

                                                binding.timeAgo00.text = show
                                                binding.channelName00.text = channelName
                                                Picasso.get().load(data.thumbnailUrl)
                                                    .into(binding.background00)
                                                binding.subsCount00.text = "0"
                                                binding.likeCount00.text = "0"
                                                binding.commentCount00.text = "0"
                                                binding.description00.text = data.description
                                                if(data.description.isEmpty()) binding.description00.visibility = View.GONE

                                                // show video
                                                binding.videoView.setVideoURI(Uri.parse(data.videoUrl))
                                                binding.videoView.setMediaController(mc)
                                                Log.d("abc", "onDataChange: 95")
                                                binding.videoView.setOnPreparedListener{
                                                    Log.d("abc", "onDataChange: 97")
                                                    binding.background00.isVisible = false
                                                    binding.spinKit.isVisible = false
                                                }
                                                    binding.videoView.isVisible = true

                                                binding.videoView.start()
                                                Log.d("abc", "onDataChange: 102")
                                                binding.videoView.setOnInfoListener { _, what, _ ->
                                                    when (what) {
                                                        MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> {
                                                            // Video started rendering
                                                            Log.d("abc", "video rendering started")
                                                        }
                                                        MediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                                                            // Buffering started
                                                            Log.d("abc", "buffering started")
                                                            binding.spinKit.isVisible=true
                                                        }
                                                        MediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                                                            // Buffering ended
                                                            binding.spinKit.isVisible=false
                                                            Log.d("abc", "buffering ended")
                                                        }
                                                    }
                                                    true
                                                }
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {

                                        }

                                    })
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })

            }
        }
    }
}