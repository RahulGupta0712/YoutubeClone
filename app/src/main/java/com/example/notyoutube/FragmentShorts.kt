package com.example.notyoutube

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.notyoutube.databinding.FragmentShortsBinding
import com.github.ybq.android.spinkit.style.Wave
import com.shashank.sony.fancytoastlib.FancyToast
import render.animations.Fade
import render.animations.Render
import render.animations.Zoom

class FragmentShorts(var datalist: ArrayList<shortsDataModel> = shortsDataList().getData()) : Fragment() {
    private lateinit var binding :FragmentShortsBinding
    private lateinit var adapter: shortsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        var activity = context as AppCompatActivity

        adapter = shortsAdapter(datalist, activity)
        binding.rvShorts.layoutManager = LinearLayoutManager(activity)
        binding.rvShorts.adapter = adapter

        val snapHelper = PagerSnapHelper()  // added this so as to scroll to only next item, not go beyond it directly
        snapHelper.attachToRecyclerView(binding.rvShorts)
    }

    companion object {

    }


}