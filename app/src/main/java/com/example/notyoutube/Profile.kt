package com.example.notyoutube

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.NumberPicker.OnValueChangeListener
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.notyoutube.ProfileFragments.ProfileCommunityFragment
import com.example.notyoutube.ProfileFragments.ProfileHomeFragment
import com.example.notyoutube.ProfileFragments.ProfileLiveFragment
import com.example.notyoutube.ProfileFragments.ProfilePlaylistsFragment
import com.example.notyoutube.ProfileFragments.ProfileShortsFragment
import com.example.notyoutube.ProfileFragments.ProfileVideosFragment
import com.example.notyoutube.databinding.ActivityProfileBinding
import com.example.notyoutube.databinding.EditChannelPopupBinding
import com.example.notyoutube.databinding.ProfileVideosBinding
import com.github.ybq.android.spinkit.style.Wave
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
import kotlin.properties.Delegates

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

        // hide the screen till you get the channel name and username
        binding.root.visibility = View.INVISIBLE

        // initialise firebase variables
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference

        // show channel name from database
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val ref = databaseRef.child("users").child(currentUser.uid)
            ref.child("Channel Name").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val channel_name = snapshot.getValue<String>()
                    cnt++
                    if(channel_name != null) {
                        binding.channelName.text = channel_name // showing the channel name
                    }
                    else{
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
                    if(user_name != null){
                        binding.username.text = user_name   // showing the username
                    }
                    else{
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

                    if(path != null) {
                        Picasso.get().load(path).into(binding.profilePicture)
                        cnt++
                    }
                    else{
                        ref.child("Profile Picture").setValue("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSUpsDK5dkH7envHCdUECqq0XzCWK1Dv96XcQ&s")
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
                    if(path != null) {
                        Picasso.get().load(path).into(binding.coverPicture)
                        cnt++
                    }
                    else{
                        cnt++
                    }
                    showActivity()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }

        binding.editChannelButton.setOnClickListener {
            val edit_channel = EditChannelPopupBinding.inflate(LayoutInflater.from(this))
            edit_channel.newChannelName.setText(binding.channelName.text.toString())
            edit_channel.newUsernameChannel.setText(binding.username.text.toString())

            val dialog = AlertDialog.Builder(this)
                .setView(edit_channel.root)
                .setTitle("Edit Channel Details")
                .setPositiveButton("SAVE") { it, _ ->
                    val newUsername = edit_channel.newUsernameChannel.text.toString()
                    val newChannelName = edit_channel.newChannelName.text.toString()
                    updateDatabase(newUsername, newChannelName)
                    it.dismiss()
                }
                .setNegativeButton("Cancel") { it, _ ->
                    it.dismiss()
                }
                .show()
        }

        adapter2 = dataAdapter(dataStore().getData(), this)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter2
        binding.recyclerView.setItemAnimator(null);

        fragment = ProfileHomeFragment()    // default
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
                        fragment = ProfileHomeFragment()
                        val trans = supportFragmentManager.beginTransaction()
                        trans.replace(R.id.frame, fragment)
                        trans.addToBackStack("home")
                        trans.commit()
                    }
                }

                1 -> {
                    if (lastFragment.name != "videos") {
                        fragment = ProfileVideosFragment()
                        val trans = supportFragmentManager.beginTransaction()
                        trans.replace(R.id.frame, fragment)
                        trans.addToBackStack("videos")
                        trans.commit()
                    }
                }

                2 -> {
                    if (lastFragment.name != "shorts") {
                        fragment = ProfileShortsFragment()
                        val trans = supportFragmentManager.beginTransaction()
                        trans.replace(R.id.frame, fragment)
                        trans.addToBackStack("shorts")
                        trans.commit()
                    }
                }

                3 -> {
                    if (lastFragment.name != "live") {
                        fragment = ProfileLiveFragment()
                        val trans = supportFragmentManager.beginTransaction()
                        trans.replace(R.id.frame, fragment)
                        trans.addToBackStack("live")
                        trans.commit()
                    }
                }

                4 -> {
                    if (lastFragment.name != "playlists") {
                        fragment = ProfilePlaylistsFragment()
                        val trans = supportFragmentManager.beginTransaction()
                        trans.replace(R.id.frame, fragment)
                        trans.addToBackStack("playlists")
                        trans.commit()
                    }
                }

                5 -> {
                    if (lastFragment.name != "community") {
                        fragment = ProfileCommunityFragment()
                        val trans = supportFragmentManager.beginTransaction()
                        trans.replace(R.id.frame, fragment)
                        trans.addToBackStack("community")
                        trans.commit()
                    }
                }

                else -> {
                    if (lastFragment.name != "home") {
                        fragment = ProfileHomeFragment()
                        val trans = supportFragmentManager.beginTransaction()
                        trans.replace(R.id.frame, fragment)
                        trans.addToBackStack("home")
                        trans.commit()
                    }
                }
            }
        }

        binding.frameProfile.bringToFront()

        // profile picture menu
        binding.profilePicture.setOnClickListener{
            val profilePicMenu = PopupMenu(this, it)
            profilePicMenu.menuInflater.inflate(R.menu.menu_profile_picture, profilePicMenu.menu)
            profilePicMenu.show()

            profilePicMenu.setOnMenuItemClickListener { item ->
                when(item.itemId){
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

        // cover picture menu
        binding.coverPicture.setOnClickListener{
            val coverPicMenu = PopupMenu(this, it)
            coverPicMenu.menuInflater.inflate(R.menu.menu_cover_pic, coverPicMenu.menu)
            coverPicMenu.show()

            coverPicMenu.setOnMenuItemClickListener { item ->
                when(item.itemId){
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

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK){
            if(result.data != null){
                val ref = Firebase.storage.reference.child("ProfilePics/" + System.currentTimeMillis().toString() + "_" + UUID.randomUUID())
                FancyToast.makeText(this, "Profile Picture is updating", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
                ref.putFile(result.data!!.data!!)
                    .addOnSuccessListener {
                        FancyToast.makeText(this, "Profile Picture updated successfully", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                        ref.downloadUrl
                            .addOnSuccessListener { url ->
                                // add url to current user's database
                                addToDatabase(url, "Profile")
                            }
                    }
            }
        }

    }
    private val coverlauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK){
            if(result.data != null){
                val ref = Firebase.storage.reference.child("CoverPics/" + System.currentTimeMillis().toString() + "_" + UUID.randomUUID())
                FancyToast.makeText(this, "Cover Picture is updating", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
                ref.putFile(result.data!!.data!!)
                    .addOnSuccessListener {
                        FancyToast.makeText(this, "Cover Picture updated successfully", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                        ref.downloadUrl
                            .addOnSuccessListener { url ->
                                // add url to current user's database
                                addToDatabase(url, "Cover")
                            }
                    }
            }
        }

    }

    private fun addToDatabase(url: Uri?, type:String) {
        val user = auth.currentUser
        user?.let {
            databaseRef.child("users").child(user.uid).child("$type Picture").setValue(url.toString())
        }
    }

    private fun showActivity() {
        if (cnt == 4) {
            // username and channel name and profile pic and cover pic are retrieved, show the activity
            binding.root.visibility = View.VISIBLE
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