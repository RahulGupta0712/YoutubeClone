package com.example.notyoutube.ProfileFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notyoutube.DataAdapterPlaylists
import com.example.notyoutube.DataListPlaylist
import com.example.notyoutube.Profile
import com.example.notyoutube.R
import com.example.notyoutube.dataAdapter
import com.example.notyoutube.dataStore
import com.example.notyoutube.databinding.FragmentProfilePlaylistsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfilePlaylistsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfilePlaylistsFragment : Fragment() {

    private lateinit var binding: FragmentProfilePlaylistsBinding
    private lateinit var Adapter:DataAdapterPlaylists

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfilePlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Adapter = DataAdapterPlaylists(context as AppCompatActivity, DataListPlaylist().getData())
        binding.rvPlaylists.layoutManager = LinearLayoutManager(context as AppCompatActivity)
        binding.rvPlaylists.adapter = Adapter

        val datalist = listOf("Date added", "Last video added")
        val Adapter = ArrayAdapter(context as AppCompatActivity, android.R.layout.simple_spinner_item, datalist)
        Adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        binding.spinner5.adapter = Adapter

        binding.spinner5.onItemSelectedListener = object :OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(context as AppCompatActivity, "Sorting by : ${parent?.getItemAtPosition(position)}", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

    }



    companion object {

    }
}