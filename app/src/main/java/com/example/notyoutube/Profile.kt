package com.example.notyoutube


import android.content.Intent
import android.net.Uri

import android.os.Bundle
import android.util.Log

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View

import android.widget.PopupMenu

import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.LinearLayoutManager

import com.example.notyoutube.ProfileFragments.ProfileCommunityFragment
import com.example.notyoutube.ProfileFragments.ProfileHomeFragment
import com.example.notyoutube.ProfileFragments.ProfileLiveFragment
import com.example.notyoutube.ProfileFragments.ProfilePlaylistsFragment
import com.example.notyoutube.ProfileFragments.ProfileShortsFragment
import com.example.notyoutube.ProfileFragments.ProfileVideosFragment
import com.example.notyoutube.databinding.ActivityProfileBinding
import com.example.notyoutube.databinding.EditChannelPopupBinding

import com.github.ybq.android.spinkit.style.DoubleBounce

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.storage.storage
import com.shashank.sony.fancytoastlib.FancyToast
import com.squareup.picasso.Picasso
import java.util.UUID

class Profile : AppCompatActivity() {
    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }

    private lateinit var adapter2: dataAdapter
    private lateinit var fragment: Fragment
    private lateinit var databaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    var cnt = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // initialise firebase variables
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference

        val currentUser = auth.currentUser

        var channelId = intent.getStringExtra("channelId")


        if (channelId == null) {
            channelId =
                currentUser!!.uid // if channelId is null, then we have come here after successful sign in, so user must be present here
        }
        else{
            // since channelId is not null, check if user with this channelId is the owner
            if (currentUser == null || currentUser.uid != channelId) {
                // show manage videos and update features to owner only
                binding.manageVideos.isVisible = false
                binding.cardView6.isVisible = false
                binding.cardView7.isVisible = false
            }
        }

        var subsCnt = 0L
        var vidCnt = 0L

        binding.subsAndVid.text = "$subsCnt subscribers . $vidCnt videos"


        databaseRef.child("users").child(channelId).child("SubscribersCount").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Long::class.java)
                value?.let{
                    subsCnt = value
                    binding.subsAndVid.text = "$subsCnt subscribers . $vidCnt videos"
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        databaseRef.child("users").child(channelId).child("VideosCount").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Long::class.java)
                value?.let{
                    vidCnt = value
                    binding.subsAndVid.text = "$subsCnt subscribers . $vidCnt videos"
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        // hide the screen till you get the channel name and username
        binding.main20020.visibility = View.INVISIBLE
        binding.progressBar7.isVisible = true
        binding.progressBar7.indeterminateDrawable = DoubleBounce()

        // show channel details from database
        val ref = databaseRef.child("users").child(channelId)
        ref.child("Channel Name").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val channel_name = snapshot.getValue<String>()
                cnt++
                if (channel_name != null) {
                    binding.channelName.text = channel_name // showing the channel name
                } else {
                    ref.child("Channel Name").setValue("Channel Name")
                }
                showActivity()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        ref.child("Username").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user_name = snapshot.getValue<String>()
                cnt++
                if (user_name != null) {
                    binding.username.text = user_name   // showing the username
                } else {
                    ref.child("Username").setValue("username")
                }
                showActivity()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        ref.child("Profile Picture").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val path = snapshot.getValue<String>()

                if (path != null) {
                    Picasso.get().load(path).into(binding.profilePicture)
                    cnt++
                } else {
                    ref.child("Profile Picture")
                        .setValue("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSUpsDK5dkH7envHCdUECqq0XzCWK1Dv96XcQ&s")   // google sign-in
                    cnt++
                }
                showActivity()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        ref.child("Cover Picture").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val path = snapshot.getValue<String>()
                if (path != null) {
                    Picasso.get().load(path).into(binding.coverPicture)
                    cnt++
                } else {
                    cnt++
                }
                showActivity()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


        binding.editChannelButton.setOnClickListener {
            if (currentUser != null && channelId == currentUser.uid) {
                val edit_channel = EditChannelPopupBinding.inflate(LayoutInflater.from(this))
                edit_channel.newChannelName.setText(binding.channelName.text.toString())
                edit_channel.newUsernameChannel.setText(binding.username.text.toString())

                val dialog = AlertDialog.Builder(this)
                    .setView(edit_channel.root)
                    .setTitle("Edit Channel Details")
                    .setPositiveButton("SAVE") { ite, _ ->
                        val newUsername = edit_channel.newUsernameChannel.text.toString()
                        val newChannelName = edit_channel.newChannelName.text.toString()
                        updateDatabase(newUsername, newChannelName)
                        ite.dismiss()
                    }
                    .setNegativeButton("Cancel") { ite, _ ->
                        ite.dismiss()
                    }

                dialog.show()
            }
        }

        adapter2 = dataAdapter(dataStore().getData(), this)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter2
        binding.recyclerView.setItemAnimator(null)

        fragment = ProfileHomeFragment(channelId)    // default
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack("home")
        transaction.replace(R.id.frame, fragment)
        transaction.commit()

        adapter2.onItemClick = { pos -> // definition of click listener
            val lastFragment =
                supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1)  // no exception will occur as it will have at least 1 fragment which is home
            when (pos) {
                0 -> {
                    if (lastFragment.name != "home") {
                        fragment = ProfileHomeFragment(channelId)
                        val trans = supportFragmentManager.beginTransaction()
                        trans.replace(R.id.frame, fragment)
                        trans.addToBackStack("home")
                        trans.commit()
                    }
                }

                1 -> {
                    if (lastFragment.name != "videos") {
                        fragment = ProfileVideosFragment(channelId)
                        val trans = supportFragmentManager.beginTransaction()
                        trans.replace(R.id.frame, fragment)
                        trans.addToBackStack("videos")
                        trans.commit()
                    }
                }

                2 -> {
                    if (lastFragment.name != "shorts") {
                        fragment = ProfileShortsFragment(channelId)
                        val trans = supportFragmentManager.beginTransaction()
                        trans.replace(R.id.frame, fragment)
                        trans.addToBackStack("shorts")
                        trans.commit()
                    }
                }

                3 -> {
                    if (lastFragment.name != "live") {
                        fragment = ProfileLiveFragment(channelId)
                        val trans = supportFragmentManager.beginTransaction()
                        trans.replace(R.id.frame, fragment)
                        trans.addToBackStack("live")
                        trans.commit()
                    }
                }

                4 -> {
                    if (lastFragment.name != "playlists") {
                        fragment = ProfilePlaylistsFragment(channelId)
                        val trans = supportFragmentManager.beginTransaction()
                        trans.replace(R.id.frame, fragment)
                        trans.addToBackStack("playlists")
                        trans.commit()
                    }
                }

                5 -> {
                    if (lastFragment.name != "community") {
                        fragment = ProfileCommunityFragment(channelId)
                        val trans = supportFragmentManager.beginTransaction()
                        trans.replace(R.id.frame, fragment)
                        trans.addToBackStack("community")
                        trans.commit()
                    }
                }

                else -> {
                    if (lastFragment.name != "home") {
                        fragment = ProfileHomeFragment(channelId)
                        val trans = supportFragmentManager.beginTransaction()
                        trans.replace(R.id.frame, fragment)
                        trans.addToBackStack("home")
                        trans.commit()
                    }
                }
            }
        }


        // profile picture menu
        binding.profilePicture.setOnClickListener {
            if (currentUser != null && channelId == currentUser.uid) {
                val profilePicMenu = PopupMenu(this, it)
                profilePicMenu.menuInflater.inflate(
                    R.menu.menu_profile_picture,
                    profilePicMenu.menu
                )
                profilePicMenu.show()

                profilePicMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.changeProfilePicture -> {
                            val intent = Intent(Intent.ACTION_PICK)
                            intent.type = "image/*"
                            launcher.launch(intent)
                            true
                        }

                        else -> false
                    }
                }
            }
        }

        // cover picture menu
        binding.coverPicture.setOnClickListener {
            if (currentUser != null && currentUser.uid == channelId) {
                val coverPicMenu = PopupMenu(this, it)
                coverPicMenu.menuInflater.inflate(R.menu.menu_cover_pic, coverPicMenu.menu)
                coverPicMenu.show()

                coverPicMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.changeCoverPicture -> {
                            val intent = Intent(Intent.ACTION_PICK)
                            intent.type = "image/*"
                            coverlauncher.launch(intent)
                            true
                        }

                        else -> false
                    }
                }
            }
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    val ref = Firebase.storage.reference.child(
                        "ProfilePics/" + System.currentTimeMillis()
                            .toString() + "_" + UUID.randomUUID()
                    )
                    FancyToast.makeText(
                        this,
                        "Profile Picture is updating",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.INFO,
                        false
                    ).show()
                    ref.putFile(result.data!!.data!!)
                        .addOnSuccessListener {
                            FancyToast.makeText(
                                this,
                                "Profile Picture updated successfully",
                                FancyToast.LENGTH_SHORT,
                                FancyToast.SUCCESS,
                                false
                            ).show()
                            ref.downloadUrl
                                .addOnSuccessListener { url ->
                                    // add url to current user's database
                                    addToDatabase(url, "Profile")
                                }
                        }
                }
            }

        }
    private val coverlauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    val ref = Firebase.storage.reference.child(
                        "CoverPics/" + System.currentTimeMillis()
                            .toString() + "_" + UUID.randomUUID()
                    )
                    FancyToast.makeText(
                        this,
                        "Cover Picture is updating",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.INFO,
                        false
                    ).show()
                    ref.putFile(result.data!!.data!!)
                        .addOnSuccessListener {
                            FancyToast.makeText(
                                this,
                                "Cover Picture updated successfully",
                                FancyToast.LENGTH_SHORT,
                                FancyToast.SUCCESS,
                                false
                            ).show()
                            ref.downloadUrl
                                .addOnSuccessListener { url ->
                                    // add url to current user's database
                                    addToDatabase(url, "Cover")
                                }
                        }
                }
            }

        }

    private fun addToDatabase(url: Uri?, type: String) {
        val user = auth.currentUser
        user?.let {
            databaseRef.child("users").child(user.uid).child("$type Picture")
                .setValue(url.toString())
        }
    }

    private fun showActivity() {
        if (cnt == 4) {
            // username and channel name and profile pic and cover pic are retrieved, show the activity
            binding.main20020.visibility = View.VISIBLE
            binding.progressBar7.isVisible = false
        }
    }

    private fun updateDatabase(newUsername: String, newChannelName: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            databaseRef.child("users").child(currentUser.uid).child("Channel Name")
                .setValue(newChannelName)
            databaseRef.child("users").child(currentUser.uid).child("Username")
                .setValue(newUsername)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // remove all fragments of home, videos, etc...
        var size = supportFragmentManager.backStackEntryCount
        while (size > 0) {
            supportFragmentManager.popBackStackImmediate()
            size--
        }
        return super.onKeyDown(keyCode, event)
    }

}