package com.example.notyoutube

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notyoutube.databinding.FragmentHomeBinding
import com.example.notyoutube.databinding.FragmentSubsChannelBinding


class subsChannelFragment(var channelName : String, var profile : Int) : Fragment() {
    private lateinit var binding:FragmentSubsChannelBinding
    private lateinit var _adapter : adapter_home_video
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =FragmentSubsChannelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var data = dataStorage().getData()

        if(channelName.isNotEmpty()) {
            for (i in data) {
                i.channelName = channelName
                i.profile = profile
            }
        }

        _adapter = adapter_home_video(data, context as AppCompatActivity)
        binding.rvVideosSubs.layoutManager = LinearLayoutManager(context as AppCompatActivity)
        binding.rvVideosSubs.adapter = _adapter
    }

    companion object {

    }
}