package com.example.notyoutube

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.MediaController
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope

import com.example.notyoutube.databinding.ActivityVideoFullModeProfileBinding

import com.github.ybq.android.spinkit.style.Circle

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.shashank.sony.fancytoastlib.FancyToast
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class videoFullModeProfile : AppCompatActivity() {
    private val binding: ActivityVideoFullModeProfileBinding by lazy {
        ActivityVideoFullModeProfileBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.progressBar9.isVisible=true
        binding.group2.isVisible=false


        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        databaseRef = FirebaseDatabase.getInstance().reference

        val progressBar =
            findViewById<View>(R.id.spin_kit) as ProgressBar    // spin_kit is id of the loader (you can customise it)
        progressBar.indeterminateDrawable = Circle()

        val videoId = intent.getStringExtra("videoId")!!
        val channelId = intent.getStringExtra("channelId")!!
        val mc = MediaController(this@videoFullModeProfile)
        mc.setAnchorView(binding.videoView)

        databaseRef.child("users").child(channelId).child("Videos").child(videoId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(DataModelVideoDetails::class.java)

                data?.let{
                    Picasso.get().load(data.profileUrl).into(binding.profile00)
                    binding.title200.text = data.title
                    binding.viewCount00.text = getString(R.string.zero)
                    val time = (System.currentTimeMillis() - data.timePosted) / 1000
                    val minutes = time / 60
                    val hour = minutes / 60
                    val days = hour / 24
                    val month = days / 30
                    val year = days / 365

                    val show =
                        if (year > 0) "$year year(s)"
                        else if (month > 0) "$month month(s)"
                        else if (days > 0) "$days day(s)"
                        else if (hour > 0) "$hour hr"
                        else if (minutes > 0) "$minutes min"
                        else "$time sec"

                    binding.timeAgo00.text = show
                    binding.channelName00.text = data.channelName
                    Picasso.get().load(data.thumbnailUrl).into(binding.background00)
                    binding.subsCount00.text = getString(R.string.zero)
                    binding.likeCount00.text = getString(R.string.zero)
                    binding.commentCount00.text = getString(R.string.zero)
                    binding.description00.text = data.description
                    if (data.description.isEmpty()) binding.description00.visibility = View.GONE

                    /// show the screen after getting the data
                    binding.progressBar9.isVisible=false
                    binding.group2.isVisible=true

                    // showing subscribe button status
                    if (user != null) {
                        if (user.uid == channelId) {
                            // this video is uploaded by same user which is viewing currently
                            binding.sub00.isVisible = false
                        } else {
                            // search if this channel is subscribed by user, if it is, then show Subscribed, else Subscribe
                            checkSubscribed(channelId, user.uid)
                        }
                    }

                    // show subscribers count
                    databaseRef.child("users").child(channelId).child("SubscribersCount").addValueEventListener(object:ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val subsCount = snapshot.getValue(Long::class.java)
                            subsCount?.let{
                                binding.subsCount00.text = subsCount.toString()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })


                    // show video
                    binding.videoView.setVideoURI(Uri.parse(data.videoUrl))
                    binding.videoView.setMediaController(mc)

                    binding.videoView.setOnPreparedListener {
                        binding.background00.isVisible = false
                        binding.spinKit.isVisible = false
                    }
                    binding.videoView.isVisible = true

                    binding.videoView.start()

                    binding.videoView.setOnInfoListener { _, what, _ ->
                        when (what) {
                            MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> {
                                // Video started rendering

                            }

                            MediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                                // Buffering started

                                binding.spinKit.isVisible = true
                            }

                            MediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                                // Buffering ended
                                binding.spinKit.isVisible = false

                            }
                        }
                        true
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.sub00.setOnClickListener {
            if (user == null) {
                FancyToast.makeText(
                    this,
                    "Sign in to Subscribe",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            } else {
                if (binding.sub00.text.toString() == "Subscribed") {
                    // unsubscribe the channel --- DELETE operation
                    databaseRef.child("users").child(user.uid).child("Subscribed Channels").child(channelId).removeValue()
                    databaseRef.child("users").child(channelId).child("Subscribers").child(user.uid).removeValue()

                    // decrease subs
                     databaseRef.child("users").child(channelId).child("SubscribersCount").get().addOnSuccessListener {
                        val sc = it.value.toString().toLong()
                        databaseRef.child("users").child(channelId).child("SubscribersCount").setValue(sc-1)
                    }

                    FancyToast.makeText(this, "Channel Unsubscribed", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                    // display as unsubscribed
                    binding.sub00.text = getString(R.string.subscribe)
                    binding.sub00.backgroundTintList = ContextCompat.getColorStateList(
                        this@videoFullModeProfile,
                        R.color.azure
                    )
                    binding.sub00.setTextColor(Color.BLACK)
                } else {
                    // subscribe the channel --- CREATE operation
                    databaseRef.child("users").child(user.uid).child("Subscribed Channels").child(channelId).setValue(channelId)
                    databaseRef.child("users").child(channelId).child("Subscribers").child(user.uid).setValue(user.uid)

                    // increase subs
                    databaseRef.child("users").child(channelId).child("SubscribersCount").get().addOnSuccessListener {
                        val sc = it.value.toString().toLong()
                        databaseRef.child("users").child(channelId).child("SubscribersCount").setValue(sc+1)
                    }


                    // channelId is the key as well as value
                    FancyToast.makeText(this, "Channel Subscribed", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                    // display as subscribed
                    binding.sub00.text = getString(R.string.subscribed)
                    binding.sub00.backgroundTintList = ContextCompat.getColorStateList(
                        this@videoFullModeProfile,
                        R.color.red
                    )
                    binding.sub00.setTextColor(Color.WHITE)
                }
            }
        }

        // show comments
        binding.imageView.setOnClickListener{
            val trans = supportFragmentManager.beginTransaction()
            trans.replace(
                R.id.frameForComments,
                ShortsCommentFragment(videoId, channelId, true)
            )
            trans.addToBackStack("comment fragment")
            trans.commit()
        }

        // show profile
        binding.profile00.setOnClickListener{
            val intent = Intent(this, Profile::class.java)
            intent.putExtra("channelId", channelId)
            startActivity(intent)
        }
        binding.channelName00.setOnClickListener{
            val intent = Intent(this, Profile::class.java)
            intent.putExtra("channelId", channelId)
            startActivity(intent)
        }
    }

    private fun checkSubscribed(channelId: String, user: String) {
        databaseRef.child("users").child(user).child("Subscribed Channels")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children) {
                        val id = snap.getValue(String::class.java)
                        id?.let {
                            if (id == channelId) {
                                // channel is subscribed by user
                                binding.sub00.text = getString(R.string.subscribed)
                                binding.sub00.backgroundTintList = ContextCompat.getColorStateList(
                                    this@videoFullModeProfile,
                                    R.color.red
                                )
                                binding.sub00.setTextColor(Color.WHITE)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            // go to home --- did this because there was a glitch when you unsubscribe a channel when you are at subscriptions page
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}