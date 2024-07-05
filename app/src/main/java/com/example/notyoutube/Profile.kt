package com.example.notyoutube

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notyoutube.ProfileFragments.ProfileCommunityFragment
import com.example.notyoutube.ProfileFragments.ProfileHomeFragment
import com.example.notyoutube.ProfileFragments.ProfileLiveFragment
import com.example.notyoutube.ProfileFragments.ProfilePlaylistsFragment
import com.example.notyoutube.ProfileFragments.ProfileShortsFragment
import com.example.notyoutube.ProfileFragments.ProfileVideosFragment
import com.example.notyoutube.databinding.ActivityProfileBinding
import com.example.notyoutube.databinding.ProfileVideosBinding
import kotlin.properties.Delegates

class Profile : AppCompatActivity() {
    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }

    private lateinit var adapter2: dataAdapter
    private lateinit var fragment : Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)


        adapter2 = dataAdapter(dataStore().getData(), this)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter2

        fragment = ProfileHomeFragment()    // default
        var trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.frame, fragment)
        trans.commit()



        adapter2.onItemClick = { pos -> // definition of click listener
            when(pos){
                0 -> {
                    fragment = ProfileHomeFragment()
                    var trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.frame, fragment)
                    trans.commit()
                }
                1 -> {

                    fragment = ProfileVideosFragment()
                    var trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.frame, fragment)
                    trans.commit()
                }
                2 -> {
                    fragment = ProfileShortsFragment()
                    var trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.frame, fragment)
                    trans.commit()
                }
                3 -> {
                    fragment = ProfileLiveFragment()
                    var trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.frame, fragment)
                    trans.commit()
                }
                4 -> {
                    fragment = ProfilePlaylistsFragment()
                    var trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.frame, fragment)
                    trans.commit()
                }
                5 -> {
                    fragment = ProfileCommunityFragment()
                    var trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.frame, fragment)
                    trans.commit()
                }
                else -> {
                    fragment = ProfileHomeFragment()
                    var trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.frame, fragment)
                    trans.commit()
                }
            }
        }





    }

}