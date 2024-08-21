package com.example.notyoutube

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.notyoutube.databinding.FragmentSubsChannelBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



class subsChannelFragment(private var channelId: String, var channelName: String, var profile: String) :
    Fragment() {
    private lateinit var binding: FragmentSubsChannelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSubsChannelBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
        return binding.root
    }

    private lateinit var _adapter: adapter_home_video
    private lateinit var datalist: ArrayList<DataModelVideoDetails>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datalist = ArrayList()
        _adapter = adapter_home_video(datalist, context as AppCompatActivity)
        binding.rvVideosSubs.layoutManager = LinearLayoutManager(context as AppCompatActivity)
        binding.rvVideosSubs.adapter = _adapter

        databaseRef.child("users").child(channelId).child("Videos")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    datalist.clear()

                    for (snap in snapshot.children) {
                        val video = snap.getValue(DataModelVideoDetails::class.java)
                        video?.let {
                            if (video.visibility == "Public")    // taking only public videos
                                datalist.add(video)
                        }
                    }

                    _adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

}