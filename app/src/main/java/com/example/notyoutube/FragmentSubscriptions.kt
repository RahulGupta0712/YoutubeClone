package com.example.notyoutube

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.notyoutube.databinding.FragmentSubscriptionsBinding
import kotlin.math.max


class FragmentSubscriptions : Fragment() {
    private lateinit var adapterSubs : dataAdapterSubs
    private lateinit var binding : FragmentSubscriptionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSubscriptionsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = context as AppCompatActivity
        adapterSubs = dataAdapterSubs(dataListSubs().getData(), activity)
        binding.rvChannelsSubscribed.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.rvChannelsSubscribed.adapter = adapterSubs

        val trans = activity.supportFragmentManager.beginTransaction()
        trans.replace(R.id.frame_feed_subs, subsChannelFragment("", 1))
        trans.addToBackStack("fragment")
        trans.commit()
    }

    companion object {

    }

}