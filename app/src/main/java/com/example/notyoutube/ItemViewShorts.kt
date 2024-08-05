package com.example.notyoutube

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.notyoutube.databinding.ItemViewShortsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.squareup.picasso.Picasso

class ItemViewShorts : AppCompatActivity() {
    private val binding by lazy{
        ItemViewShortsBinding.inflate(layoutInflater)
    }

    private lateinit var auth:FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.menuShortsButton.bringToFront()
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
        val data = intent.getParcelableExtra<DataModelVideoDetails>("data")
        if(data != null){
            val user = auth.currentUser
            if(user != null){
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
                                                Picasso.get().load(profilePicUri).into(binding.profileShorts)
                                                binding.titleShorts.text = data.title
                                                binding.ChannelNameShorts.text = channelName
                                                Picasso.get().load(data.thumbnailUrl).into(binding.shortsBackgroundVideo)

                                                binding.likeCountShorts.text = "0"
                                                binding.commentCountShorts.text = "0"

                                                // show shorts
                                                binding.shortsView.setVideoURI(Uri.parse(data.videoUrl))
                                                binding.shortsView.start()

                                                binding.songNameShorts.text = "Original Audio"

                                                binding.shortsView.setOnPreparedListener{
                                                    binding.shortsBackgroundVideo.isVisible = false
                                                    binding.progressBar3.isVisible=false
                                                }

                                                binding.shortsView.isVisible = true


                                                binding.shortsView.setOnInfoListener { _, what, _ ->
                                                    when (what) {
                                                        MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> {
                                                            // Video started rendering
                                                            Log.d("abc", "video rendering started")
                                                        }
                                                        MediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                                                            // Buffering started
                                                            Log.d("abc", "buffering started")
                                                            binding.progressBar3.isVisible=true
                                                        }
                                                        MediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                                                            // Buffering ended
                                                            Log.d("abc", "buffering ended")
                                                            binding.progressBar3.isVisible=false
                                                        }
                                                    }
                                                    true
                                                }

                                                binding.shortsView.setOnCompletionListener {
                                                    binding.shortsView.start()
                                                    binding.progressBar3.isVisible = true
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