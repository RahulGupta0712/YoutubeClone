package com.example.notyoutube

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.notyoutube.databinding.ItemViewShortsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shashank.sony.fancytoastlib.FancyToast
import com.squareup.picasso.Picasso

class ItemViewShorts : AppCompatActivity() {
    private val binding by lazy {
        ItemViewShortsBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.menuShortsButton.bringToFront()
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
        val data = intent.getParcelableExtra<DataModelVideoDetails>("data")!!

        val user = auth.currentUser
        if (user != null) {

            Picasso.get().load(data.profileUrl).into(binding.profileShorts)
            binding.titleShorts.text = data.title
            binding.ChannelNameShorts.text = data.channelName
            Picasso.get().load(data.thumbnailUrl).into(binding.shortsBackgroundVideo)

            binding.likeCountShorts.text = getString(R.string.zero)
            binding.commentCountShorts.text = getString(R.string.zero)

            // show shorts
            binding.shortsView.setVideoURI(Uri.parse(data.videoUrl))
            binding.shortsView.start()

            binding.songNameShorts.text = getString(R.string.original_audio)

            binding.shortsView.setOnPreparedListener {
                binding.shortsBackgroundVideo.isVisible = false
                binding.progressBar3.isVisible = false
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
                        binding.progressBar3.isVisible = true
                    }

                    MediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                        // Buffering ended
                        Log.d("abc", "buffering ended")
                        binding.progressBar3.isVisible = false
                    }
                }
                true
            }

            binding.shortsView.setOnCompletionListener {
                binding.shortsView.start()
                binding.progressBar3.isVisible = true
            }


            // don't show the subscribe button if already subscribed or shorts is uploaded by same user
            if (user.uid == data.channelId) {
                // same channel
                binding.subscribeButtonShorts.isVisible = false
            } else {
                // check if subscribed
                databaseRef.child("users").child(user.uid).child("Subscribed Channels")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (snap in snapshot.children) {
                                val id = snap.getValue(String::class.java)
                                id?.let {
                                    if (id == data.channelId) {
                                        // subscribed, don't show
                                        binding.subscribeButtonShorts.isVisible = false
                                    }
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })

            }
        }

        binding.subscribeButtonShorts.setOnClickListener {
            if (user == null) {
                // not signed in
                FancyToast.makeText(
                    this,
                    "Sign in to subscribe",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            } else {
                // subscribe
                FancyToast.makeText(
                    this,
                    "Channel Subscribed",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.SUCCESS,
                    false
                ).show()
                databaseRef.child("users").child(user.uid).child("Subscribed Channels")
                    .child(data.channelId).setValue(data.channelId)
                binding.subscribeButtonShorts.isVisible = false
            }
        }
    }
}