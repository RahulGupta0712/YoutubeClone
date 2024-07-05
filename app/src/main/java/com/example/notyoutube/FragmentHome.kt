package com.example.notyoutube

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notyoutube.databinding.FragmentHomeBinding

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _adapter = adapter_home_video(dataStorage().getData(), context as MainActivity)

        binding.videoi.layoutManager = LinearLayoutManager(context)
        binding.videoi.adapter = _adapter
    }

    companion object {

    }
}