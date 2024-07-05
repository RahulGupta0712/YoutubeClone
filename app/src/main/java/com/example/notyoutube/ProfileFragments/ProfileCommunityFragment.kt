package com.example.notyoutube.ProfileFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notyoutube.MyVideoAdapter
import com.example.notyoutube.Profile
import com.example.notyoutube.R
import com.example.notyoutube.dataAdapter
import com.example.notyoutube.dataStore
import com.example.notyoutube.dataVideos
import com.example.notyoutube.databinding.FragmentProfileCommunityBinding
import com.example.notyoutube.databinding.FragmentVideoHomeBinding

class ProfileCommunityFragment : Fragment() {

    private lateinit var binding: FragmentProfileCommunityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }



    companion object {

    }
}