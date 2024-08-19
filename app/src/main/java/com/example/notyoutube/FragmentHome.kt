package com.example.notyoutube

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notyoutube.databinding.FragmentHomeBinding
import com.github.ybq.android.spinkit.style.CubeGrid
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class FragmentHome : Fragment() {
    private lateinit var _adapter : adapter_home_video
    private lateinit var binding:FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var datalist : ArrayList<DataModelVideoDetails>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datalist = ArrayList()
        _adapter = adapter_home_video(datalist, context as AppCompatActivity)
        binding.videoi.layoutManager = LinearLayoutManager(context)
        binding.videoi.adapter = _adapter

        binding.videoi.isVisible = false
        binding.progressBar4.isVisible = true

        binding.progressBar4.indeterminateDrawable = CubeGrid()

        Firebase.firestore.collection("Videos").get().addOnSuccessListener { data ->
            datalist.clear()

            for(doc in data){
                val video = doc.toObject(DataModelVideoDetails::class.java)
                video.videoId = doc.id
                Firebase.firestore.collection("Videos").document(video.videoId).set(video)
                datalist.add(video)
            }

            binding.videoi.isVisible = true
            binding.progressBar4.isVisible = false

            _adapter.notifyDataSetChanged()
        }
    }

    companion object {

    }
}