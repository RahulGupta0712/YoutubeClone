package com.example.notyoutube.ProfileFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notyoutube.DataAdapterPlaylists
import com.example.notyoutube.DataAdapterShortsProfile
import com.example.notyoutube.DataListPlaylist
import com.example.notyoutube.DataListShortsFragment
import com.example.notyoutube.MyVideoAdapter
import com.example.notyoutube.Profile
import com.example.notyoutube.R
import com.example.notyoutube.dataAdapter
import com.example.notyoutube.dataStore
import com.example.notyoutube.databinding.FragmentProfileCommunityBinding
import com.example.notyoutube.databinding.FragmentProfileHomeBinding

class ProfileHomeFragment : Fragment() {

    private lateinit var binding: FragmentProfileHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // popular videos
//        val data = dataVideos().getData()
//        data.shuffle()
//        val popularVideosAdapter = MyVideoAdapter(data, context as AppCompatActivity)
//        binding.popularVideosHomeProfile.layoutManager = LinearLayoutManager(context as AppCompatActivity)
//        binding.popularVideosHomeProfile.adapter = popularVideosAdapter
//
//
//        // videos
//        val VideosAdapter = MyVideoAdapter(dataVideos().getData(), context as AppCompatActivity)
//        binding.videosHomeProfile.layoutManager = LinearLayoutManager(context as AppCompatActivity)
//        binding.videosHomeProfile.adapter = VideosAdapter


        // shorts
//        val dataShorts = DataListShortsFragment().getData()
//        val shortsAdapter = DataAdapterShortsProfile(context as AppCompatActivity, dataShorts)
//        binding.shortsHomeProfile.layoutManager = GridLayoutManager(context as AppCompatActivity, 3)
//        binding.shortsHomeProfile.adapter = shortsAdapter

        // playlists

        val playlistAdapter = DataAdapterPlaylists(context as AppCompatActivity, DataListPlaylist().getData())
        binding.playlistsHomeProfile.layoutManager = LinearLayoutManager(context as AppCompatActivity)
        binding.playlistsHomeProfile.adapter = playlistAdapter

    }
    companion object {

    }
}