package com.example.notyoutube

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebViewClient
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notyoutube.databinding.ActivityVideoRecorderBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore

class Search : AppCompatActivity() {
    private val binding: ActivityVideoRecorderBinding by lazy {
        ActivityVideoRecorderBinding.inflate(layoutInflater)
    }

    private lateinit var channelList: ArrayList<String>
    private lateinit var videoList: ArrayList<DataModelVideoDetails>

    private lateinit var channelAdapter: SearchChannelAdapter
    private lateinit var videoAdapter: adapter_home_video

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        channelList = ArrayList()
        videoList = ArrayList()

        channelAdapter = SearchChannelAdapter(this, channelList)
        videoAdapter = adapter_home_video(videoList, this)

        // initialise recycler view
        binding.searchResultRV.layoutManager = LinearLayoutManager(this)

        binding.searchBar.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    // query is not null
                    // default - search channel
                    searchChannel(query)
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        binding.searchChannelButton.setOnClickListener {
            val query = binding.searchBar.query.toString()
            searchChannel(query)
        }

        binding.searchVideoButton.setOnClickListener {
            val query = binding.searchBar.query.toString()
            searchVideo(query)
        }
    }

    private fun searchVideo(videoTitleQuery: String) {
        binding.notFoundMessage.isVisible = false
        binding.searchResultRV.isVisible = false
        binding.progressBar8.isVisible = true
        val db = Firebase.firestore // search in public videos

        videoList.clear()

        db.collection("Videos")
            .whereGreaterThanOrEqualTo("title", videoTitleQuery)
            .whereLessThanOrEqualTo("title", videoTitleQuery + "\uf8ff")
        .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    val video = doc.toObject(DataModelVideoDetails::class.java)
                    videoList.add(video)
                }
                showVideos()
            }
            .addOnFailureListener{
                showVideos()
            }
    }

    private fun showVideos() {
        binding.progressBar8.isVisible = false

        if (videoList.isEmpty()) {
            binding.notFoundMessage.text = "Videos Not Found"
            binding.notFoundMessage.isVisible = true
        } else {
            // inflate the recycler view
            binding.searchResultRV.adapter = videoAdapter
            videoAdapter.notifyDataSetChanged()
            binding.searchResultRV.isVisible = true
        }
    }

    private fun searchChannel(channelNameQuery: String) {
        binding.notFoundMessage.isVisible = false
        binding.searchResultRV.isVisible = false
        binding.progressBar8.isVisible = true

        // search in realtime database
        val db = FirebaseDatabase.getInstance().reference

        db.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                channelList.clear()

                for (snap in snapshot.children) {
                    val channelName = snap.child("Channel Name").getValue(String::class.java)
                    val channelId = snap.key!!

                    channelName?.let {
                        // now check if channelNameQuery is prefix of channelName, then only add the channelId in data
                        if (channelNameQuery.length <= channelName.length) {
                            if (channelNameQuery == channelName.substring(
                                    0,
                                    channelNameQuery.length
                                )
                            ) {
                                // valid
                                channelList.add(channelId)
                            }
                        }
                    }
                }

                // show channels in recycler view
                showChannels()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun showChannels() {
        binding.progressBar8.isVisible = false
        if (channelList.isEmpty()) {
            binding.notFoundMessage.text = "Channel Not Found"
            binding.notFoundMessage.isVisible = true
        } else {
            // inflate the recycler view
            binding.searchResultRV.adapter = channelAdapter
            channelAdapter.notifyDataSetChanged()
            binding.searchResultRV.isVisible = true
        }
    }
}