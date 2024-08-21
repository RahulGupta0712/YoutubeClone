package com.example.notyoutube

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notyoutube.databinding.FragmentShortsCommentBinding

class ShortsCommentFragment(private var comments : String) : Fragment() {
    private lateinit var binding:FragmentShortsCommentBinding

    private lateinit var adapterObject:dataAdapterCommentShorts
    private lateinit var data : ArrayList<dataModelCommentShorts>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentShortsCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        data = dataListCommentShorts().getData()
        data = ArrayList()
        binding.commentCountShortsInside.text = comments
        binding.recyclerViewShortsComment.layoutManager = LinearLayoutManager(context as AppCompatActivity)
        adapterObject = dataAdapterCommentShorts(data, context as AppCompatActivity)
        binding.recyclerViewShortsComment.adapter = adapterObject

        binding.exitButton.setOnClickListener{
            val activity = context as AppCompatActivity
            val trans = activity.supportFragmentManager
            trans.popBackStack()
        }

    }

}