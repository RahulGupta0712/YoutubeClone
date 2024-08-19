package com.example.notyoutube

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.notyoutube.ProfileFragments.dataListCommentShorts
import com.example.notyoutube.databinding.FragmentSubscriptionsBinding
import com.github.ybq.android.spinkit.style.ChasingDots
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.max


class FragmentSubscriptions : Fragment() {
    private lateinit var adapterSubs: dataAdapterSubs
    private lateinit var binding: FragmentSubscriptionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSubscriptionsBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
        return binding.root
    }

    private lateinit var datalist: ArrayList<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar6.isVisible = true
        binding.frameFeedSubs.isVisible = false
        binding.rvChannelsSubscribed.isVisible = false
        binding.progressBar6.indeterminateDrawable = ChasingDots()

        val activity = context as AppCompatActivity

        datalist = ArrayList()
        adapterSubs = dataAdapterSubs(datalist, activity)
        binding.rvChannelsSubscribed.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.rvChannelsSubscribed.adapter = adapterSubs

        val user = auth.currentUser
        user?.let {
            // finding the subscribed channels by the user, if user is not signed in then he sees a blank screen
            databaseRef.child("users").child(user.uid).child("Subscribed Channels")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        datalist.clear()
                        for (snap in snapshot.children) {
                            val channelId = snap.getValue(String::class.java)
                            channelId?.let {
                                datalist.add(channelId)
                            }
                        }

                        showSubscribedFeed(datalist)

                        binding.progressBar6.isVisible = false
                        binding.frameFeedSubs.isVisible = true
                        binding.rvChannelsSubscribed.isVisible = true

                        adapterSubs.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
        }


    }

    private fun showSubscribedFeed(datalist: ArrayList<String>) {
        // show subscribed channels feed --- content from subscribed channels
        // first check if this is started for the first time
        try {
            val manager = requireActivity().supportFragmentManager
            val sz = manager.backStackEntryCount
            if (sz > 0) {
                val name = manager.getBackStackEntryAt(sz - 1).name
                if (name == "FragmentSubscriptions") {
                    val trans = manager.beginTransaction()
                    trans.replace(
                        R.id.frame_feed_subs,
                        SubscribedChannelsFeedFragment(datalist)
                    )
                    trans.commit()
                }
            }
        } catch (e: Exception) {
            Log.d("fragment", "showSubscribedFeed: activity attached is null")
        }

    }


    companion object {

    }

}