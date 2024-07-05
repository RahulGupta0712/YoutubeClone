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
    private lateinit var adapter2: dataAdapter
    private lateinit var binding: FragmentProfileCommunityBinding
    private lateinit var navController: NavController
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter2 = dataAdapter(dataStore().getData(), context as Profile)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter2

        navController = Navigation.findNavController(view)

        adapter2.onItemClick = { position ->
            when (position) {
                0 -> navController.navigate(R.id.action_profileCommunityFragment_to_profileHomeFragment)
                1 -> navController.navigate(R.id.action_profileCommunityFragment_to_profileVideosFragment)
                2 -> navController.navigate(R.id.action_profileCommunityFragment_to_profileShortsFragment)
                3 -> navController.navigate(R.id.action_profileCommunityFragment_to_profileLiveFragment)
                4 -> navController.navigate(R.id.action_profileCommunityFragment_to_profilePlaylistsFragment)
                5 -> navController.navigate(R.id.action_profileVideosFragment_to_profileCommunityFragment)
            }
        }

    }

    companion object {

    }
}