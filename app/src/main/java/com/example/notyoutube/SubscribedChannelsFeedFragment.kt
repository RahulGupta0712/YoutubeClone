package com.example.notyoutube

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notyoutube.databinding.FragmentSubscribedChannelsFeedBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SubscribedChannelsFeedFragment(private var subscribedChannels : ArrayList<String>) : Fragment() {

    private lateinit var binding:FragmentSubscribedChannelsFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSubscribedChannelsFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val videoslist = ArrayList<DataModelVideoDetails>()
        val shortslist = ArrayList<DataModelVideoDetails>()
        val databaseRef : DatabaseReference = FirebaseDatabase.getInstance().reference

        val VideoAdapter = adapter_home_video(videoslist, context as AppCompatActivity)
        binding.videosRV.adapter = VideoAdapter
        binding.videosRV.layoutManager = LinearLayoutManager(context)

        val ShortsAdapter = DataAdapterShortsSubs(shortslist, context as AppCompatActivity)
        binding.shortsRV.adapter = ShortsAdapter
        binding.shortsRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        for(channelId in subscribedChannels){
            // get all public videos by this channel
            databaseRef.child("users").child(channelId).child("Videos").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(snap in snapshot.children){
                        val video = snap.getValue(DataModelVideoDetails::class.java)
                        video?.let{
                            if(video.visibility == "Public")
                                videoslist.add(video)
                        }
                    }
                    VideoAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {}
            })

            // get all public shorts by this channel
            databaseRef.child("users").child(channelId).child("Shorts").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(snap in snapshot.children){
                        val shorts = snap.getValue(DataModelVideoDetails::class.java)
                        shorts?.let{
                            if(shorts.visibility == "Public")
                                shortslist.add(shorts)
                        }
                    }
                    ShortsAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }



    }


}