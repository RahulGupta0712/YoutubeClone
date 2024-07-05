package com.example.notyoutube

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

//        var pos = intent.getIntExtra("", 0)
//        val fragment = when (pos) {
//            0 -> ProfileHomeFragment()
//            1 -> ProfileVideosFragment()
//            2 -> ProfileShortsFragment()
//            3 -> ProfileLiveFragment()
//            4 -> ProfilePlaylistsFragment()
//            5 -> ProfileCommunityFragment()
//            else -> ProfileHomeFragment()
//        }

//        var trans = supportFragmentManager.beginTransaction()
//        trans.replace(R.id.frame, fragment)
//        trans.commit()
//
//        adapter2 = dataAdapter(dataStore().getData(), this)
//        binding.recyclerView.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        binding.recyclerView.adapter = adapter2
    }


}