package com.example.notyoutube


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.PagerSnapHelper

import com.example.notyoutube.databinding.FragmentShortsBinding
import com.github.ybq.android.spinkit.style.WanderingCubes

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

import render.animations.Render
import render.animations.Zoom

class FragmentShorts : Fragment() {
    private lateinit var binding :FragmentShortsBinding
    private lateinit var adapter: shortsAdapter
    private lateinit var datalist:ArrayList<DataModelVideoDetails>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentShortsBinding.inflate(inflater, container, false)
        val render = Render(context as AppCompatActivity)
        render.setAnimation(Zoom().In(binding.root))
        render.setDuration(300)
        render.start()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvShorts.isVisible = false
        binding.progressBar5.isVisible = true

        binding.progressBar5.indeterminateDrawable = WanderingCubes()

        val activity = context as AppCompatActivity
        datalist = ArrayList()
        adapter = shortsAdapter(datalist, activity)
        binding.rvShorts.layoutManager = LinearLayoutManager(activity)
        binding.rvShorts.adapter = adapter

        val snapHelper = PagerSnapHelper()  // added this so as to scroll to only next item, not go beyond it directly
        snapHelper.attachToRecyclerView(binding.rvShorts)

        Firebase.firestore.collection("Shorts").get().addOnSuccessListener { fullShorts ->
            datalist.clear()
            for (doc in fullShorts){
                val shorts = doc.toObject(DataModelVideoDetails::class.java)
                shorts.videoId = doc.id
                Firebase.firestore.collection("Shorts").document(shorts.videoId).set(shorts)
                datalist.add(shorts)
            }

            binding.rvShorts.isVisible = true
            binding.progressBar5.isVisible = false
            adapter.notifyDataSetChanged()
        }
    }
    


}