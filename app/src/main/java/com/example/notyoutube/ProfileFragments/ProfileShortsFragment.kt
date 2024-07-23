package com.example.notyoutube.ProfileFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notyoutube.DataAdapterShortsProfile
import com.example.notyoutube.DataListShortsFragment
import com.example.notyoutube.Profile
import com.example.notyoutube.R
import com.example.notyoutube.dataAdapter
import com.example.notyoutube.dataStore
import com.example.notyoutube.databinding.FragmentProfileShortsBinding
import com.example.notyoutube.shortsDataList
import com.example.notyoutube.shortsDataModel

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

    private lateinit var binding: FragmentProfileShortsBinding
    private lateinit var Adapter:DataAdapterShortsProfile
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
        val datalist = DataListShortsFragment().getData()

        Adapter = DataAdapterShortsProfile(context as AppCompatActivity, datalist)
        binding.rvProfileShorts.layoutManager = GridLayoutManager(context as AppCompatActivity, 3)
        binding.rvProfileShorts.adapter = Adapter

    }



    companion object {

    }
}