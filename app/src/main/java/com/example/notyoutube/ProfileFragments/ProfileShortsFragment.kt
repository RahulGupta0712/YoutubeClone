package com.example.notyoutube.ProfileFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notyoutube.Profile
import com.example.notyoutube.R
import com.example.notyoutube.dataAdapter
import com.example.notyoutube.dataStore
import com.example.notyoutube.databinding.FragmentProfileShortsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileShortsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileShortsFragment : Fragment() {
    private lateinit var adapter2: dataAdapter
    private lateinit var binding: FragmentProfileShortsBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileShortsBinding.inflate(inflater, container, false)
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
                0 -> navController.navigate(R.id.action_profileShortsFragment_to_profileHomeFragment)
                1 -> navController.navigate(R.id.action_profileShortsFragment_to_profileVideosFragment)
                3 -> navController.navigate(R.id.action_profileShortsFragment_to_profileLiveFragment)
                4 -> navController.navigate(R.id.action_profileShortsFragment_to_profilePlaylistsFragment)
                5 -> navController.navigate(R.id.action_profileShortsFragment_to_profileCommunityFragment)
            }
        }

    }

    companion object {

    }
}