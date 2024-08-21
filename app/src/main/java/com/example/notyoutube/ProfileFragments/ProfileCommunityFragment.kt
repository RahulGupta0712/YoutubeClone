package com.example.notyoutube.ProfileFragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.notyoutube.CommunityPostInfo
import com.example.notyoutube.DataAdapterCommunity2
import com.example.notyoutube.R
import com.example.notyoutube.databinding.EditPostBinding
import com.example.notyoutube.databinding.FragmentProfileCommunityBinding
import com.github.ybq.android.spinkit.style.ThreeBounce
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
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class ProfileCommunityFragment : Fragment(), DataAdapterCommunity2.OnItemClickListener {

    private lateinit var binding: FragmentProfileCommunityBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // initialise variable of firebase
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference

        // Inflate the layout for this fragment
        binding = FragmentProfileCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var Adapter2: DataAdapterCommunity2
    private lateinit var datalist: ArrayList<CommunityPostInfo>
    private lateinit var imagePostList:ArrayList<String>    // contains urls for images

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imagePostList = ArrayList()

        // loader
        val loader = binding.loaderImagePost as ProgressBar
        loader.indeterminateDrawable = ThreeBounce()

        // create operation
        binding.addPostButton.setOnClickListener {
            val post = binding.post.text.toString()
            if (post.isEmpty()) {
                FancyToast.makeText(
                    context as AppCompatActivity,
                    "Post should have at least 1 character",
                    FancyToast.LENGTH_LONG,
                    FancyToast.WARNING,
                    false
                ).show()
            } else {
                val currentUser = auth.currentUser
                currentUser?.let {
                    val ref = databaseRef.child("users").child(currentUser.uid).child("Community Posts")
                    val key = ref.push().key
                    key?.let {
                        val time = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm")
                        val formattedTime = time.format(formatter)
                        ref.child(key).setValue(CommunityPostInfo(key, post, imagePostList, formattedTime))
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context as AppCompatActivity,"Post uploaded", Toast.LENGTH_SHORT).show()
                                    binding.post.setText(getString(R.string.empty))
                                    imagePostList.clear()
                                } else {
                                    Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
                                    imagePostList.clear()
                                }
                            }
                    }
                }
            }

        }

        // read operation -- show all posts
        val currentUser = auth.currentUser
        datalist = ArrayList()

        binding.root.visibility = View.INVISIBLE

        if (currentUser != null) {
            databaseRef.child("users").child(currentUser.uid).child("Channel Name")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val channelName = snapshot.getValue<String>()
                        if (channelName != null) {
                            // retrieved channel name here
                            // now go and find profile
                            databaseRef.child("users").child(currentUser.uid)
                                .child("Profile Picture")
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {

                                        val profilePath = snapshot.getValue<String>()
                                        profilePath?.let {
                                            Adapter2 = DataAdapterCommunity2(
                                                datalist,
                                                profilePath,
                                                channelName,
                                                context as AppCompatActivity,
                                                this@ProfileCommunityFragment
                                            )
                                            binding.rvTextposts.layoutManager =
                                                LinearLayoutManager(context)
                                            binding.rvTextposts.adapter = Adapter2

                                            // now showing the community posts by this channel
                                            databaseRef.child("users").child(currentUser.uid)
                                                .child("Community Posts")
                                                .addValueEventListener(object : ValueEventListener {
                                                    @SuppressLint("NotifyDataSetChanged")
                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                        datalist.clear()
                                                        for (snap in snapshot.children) {
                                                            val post_info =
                                                                snap.getValue<CommunityPostInfo>()
                                                            post_info?.let {
                                                                datalist.add(post_info)
                                                            }
                                                        }
                                                        datalist.reverse()
                                                        Adapter2.notifyDataSetChanged()

                                                        binding.root.visibility = View.VISIBLE
                                                    }

                                                    override fun onCancelled(error: DatabaseError) {

                                                    }

                                                })
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {

                                    }

                                })
                        } else {
                            binding.root.visibility = View.VISIBLE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
        }

        binding.uploadImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            uploadImageLauncher.launch(intent)
        }
    }

    private val uploadImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imagesData = result.data!!.clipData

                MotionToast.darkColorToast(context as AppCompatActivity, "Uploading Image(s)", "Wait for all images to be uploaded", MotionToastStyle.INFO, MotionToast.GRAVITY_BOTTOM, MotionToast.LONG_DURATION, ResourcesCompat.getFont(context as AppCompatActivity, www.sanju.motiontoast.R.font.helvetica_regular))

                if (imagesData != null) {
                    // hide the post button till all images are uploaded and added to datalist
                    binding.addPostButton.visibility = View.INVISIBLE
                    binding.loaderImagePost.visibility = View.VISIBLE

                    val size = imagesData.itemCount
                    for (pos in 0..<size) {
                        val imageUri = imagesData.getItemAt(pos).uri
                        uploadImage(imageUri, pos+1, size)
                    }
                }
            }

        }

    private fun uploadImage(data: Uri?, pos:Int, size : Int) {
        val ref = Firebase.storage.reference.child("Community Post Photos/" + UUID.randomUUID() + "_" + System.currentTimeMillis().toString())
        ref.putFile(data!!)
            .addOnSuccessListener {
                ref.downloadUrl
                    .addOnSuccessListener { url ->
                        imagePostList.add(url.toString())
                        if(pos == size){
                            // make the post button visible as all images are uploaded and added in imagelist
                            binding.addPostButton.visibility = View.VISIBLE
                            binding.loaderImagePost.visibility = View.INVISIBLE
                            if(context != null) // exception can occur due to network failure of user
                                FancyToast.makeText(context as AppCompatActivity, "All images uploaded successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show()
                        }
                    }
            }
    }

    override fun onEditClick(postKey : String, postText: String, imagePostList : ArrayList<String>, postTime : String) {
        val editPost = EditPostBinding.inflate(LayoutInflater.from(context))
        editPost.newPost.setText(postText)

        SweetAlertDialog(context as AppCompatActivity, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
            .setCustomView(editPost.root)
            .setCustomImage(R.drawable.edit)
            .setTitleText("Edit Your Post Here")
            .setConfirmButton("Save") {
                val currentUser = auth.currentUser
                currentUser?.let {
                    val post = editPost.newPost.text.toString()
                    databaseRef.child("users").child(currentUser.uid).child("Community Posts")
                        .child(postKey).setValue(CommunityPostInfo(postKey, post, imagePostList, postTime))
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Post updated !", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Failed to update post !",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
                it.dismissWithAnimation()
            }
            .setCancelButton("Cancel") {
                it.dismiss()
            }
            .show()

    }

    override fun onDeleteClick(postKey: String) {
        val currentUser = auth.currentUser
        currentUser?.let {
            databaseRef.child("users").child(currentUser.uid).child("Community Posts")
                .child(postKey).removeValue()
            Toast.makeText(context, "Post Deleted Successfully", Toast.LENGTH_SHORT).show()
        }

    }
}