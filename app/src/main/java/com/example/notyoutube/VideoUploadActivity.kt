package com.example.notyoutube


import android.content.Intent

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.isVisible
import com.example.notyoutube.databinding.ActivityVideoUploadBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storage

import com.shashank.sony.fancytoastlib.FancyToast


import java.util.UUID

class VideoUploadActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityVideoUploadBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    private var videoUrl: String? = null
    private var videoLength: Long = 0    // in seconds
    private var thumbnailUrl: String? = null
    private var visibility = "Public"

    // for handling cases when video/thumbnail is uploading and activity gets destroyed --- provides more robustness to application
    private var videoUploadTask: UploadTask? = null
    private var thumbnailUploadTask: UploadTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference

        val firestore = Firebase.firestore

        if (auth.currentUser == null) {
            FancyToast.makeText(
                this,
                "Sign-in to upload a video",
                FancyToast.LENGTH_LONG,
                FancyToast.ERROR,
                false
            ).show()
        }

        // setting spinner for visibility - public, private
        val data = listOf("Public", "Private")
        val visAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        visAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        binding.visibility.adapter = visAdapter

        // update visibility
        binding.visibility.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                visibility = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.videoUploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "video/*"
            videoLauncher.launch(intent)
        }

        binding.thumbnailUploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            thumbnailLauncher.launch(intent)
        }

        binding.postButton.setOnClickListener {

//            // check condition
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
//                // When permission is granted
////                selectVideo();
//            }
//            else {
//                // When permission is not granted -> request permission
//                ActivityCompat.requestPermissions(this, arrayOf( Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),1);
//            }

            val title = binding.titleEditTextVideo.text.toString()
            val desc = binding.DescriptionEditText.text.toString()

            if (title.isEmpty()) {
                FancyToast.makeText(
                    this,
                    "Title can't be empty",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            } else if (videoUrl == null) {
                FancyToast.makeText(
                    this,
                    "Please upload video",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.WARNING,
                    false
                ).show()
            } else if (thumbnailUrl == null) {
                FancyToast.makeText(
                    this,
                    "Please upload thumbnail",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.WARNING,
                    false
                ).show()
            } else {
                // all details correct
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val type = if (videoLength <= 60) "Shorts" else "Videos"
                    val ref = databaseRef.child("users").child(currentUser.uid).child(type)
                    val key = ref.push().key
                    key?.let {
                        databaseRef.child("users").child(currentUser.uid).child("Channel Name")
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val channelName = snapshot.getValue<String>()
                                    channelName?.let {
                                        // got the channel name, now time for profile picture
                                        databaseRef.child("users").child(currentUser.uid)
                                            .child("Profile Picture")
                                            .addValueEventListener(object : ValueEventListener {
                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                    val profilePicUri = snapshot.getValue<String>()
                                                    profilePicUri?.let {
                                                        // got the channel details, now add video
                                                        val video = DataModelVideoDetails(
                                                            key,
                                                            title,
                                                            desc,
                                                            thumbnailUrl!!,
                                                            videoUrl!!,
                                                            videoLength,
                                                            System.currentTimeMillis(),
                                                            visibility, channelName, profilePicUri, currentUser.uid
                                                        )

                                                        // add in main feed --- firestore database
                                                        if (visibility == "Public") {
                                                            firestore.collection(type)
                                                                .add(video)  // depending on type - video or shorts
                                                        }


                                                        ref.child(key).setValue(video)
                                                            .addOnSuccessListener {
                                                                FancyToast.makeText(
                                                                    this@VideoUploadActivity,
                                                                    "Post successful",
                                                                    FancyToast.LENGTH_SHORT,
                                                                    FancyToast.SUCCESS,
                                                                    false
                                                                ).show()
                                                                startActivity(
                                                                    Intent(
                                                                        this@VideoUploadActivity,
                                                                        MainActivity::class.java
                                                                    )
                                                                )
                                                                finish()
                                                            }
                                                            .addOnFailureListener {
                                                                FancyToast.makeText(
                                                                    this@VideoUploadActivity,
                                                                    "Post Failed",
                                                                    FancyToast.LENGTH_SHORT,
                                                                    FancyToast.ERROR,
                                                                    false
                                                                ).show()
                                                            }


                                                    }
                                                }

                                                override fun onCancelled(error: DatabaseError) {

                                                }

                                            })
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }

                            })
                    }
                } else {
                    FancyToast.makeText(
                        this,
                        "First Sign in to upload a video",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.WARNING,
                        false
                    ).show()
                }
            }
        }

        binding.cancelPostButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private val videoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val destinationUri =
                    "Videos/" + UUID.randomUUID().toString() + "_" + System.currentTimeMillis()
                        .toString()
                val ref = Firebase.storage.reference.child(destinationUri)

                FancyToast.makeText(
                    this,
                    "Uploading Video",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.INFO,
                    false
                ).show()

                binding.progressBar.isVisible = true

                videoUploadTask = ref.putFile(result.data!!.data!!)
                videoUploadTask?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        // video uploaded successfully, save the uri in a string
                        ref.downloadUrl
                            .addOnSuccessListener { url ->
                                videoUrl = url.toString()
                                FancyToast.makeText(
                                    this,
                                    "Video Uploaded successfully",
                                    FancyToast.LENGTH_SHORT,
                                    FancyToast.SUCCESS,
                                    false
                                ).show()

                                getVideoLength(result.data!!.data!!)    // find video length
                                binding.progressBar.isVisible = false
                            }
                            .addOnFailureListener {
                                FancyToast.makeText(
                                    this,
                                    "Video Upload Failed",
                                    FancyToast.LENGTH_SHORT,
                                    FancyToast.ERROR,
                                    false
                                ).show()
                            }

                    } else {
                        FancyToast.makeText(
                            this,
                            "Video Upload Failed",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR,
                            false
                        ).show()
                    }
                }

            }
        }


    private val thumbnailLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val ref = Firebase.storage.reference.child(
                    "Thumbnails/" + UUID.randomUUID().toString() + "_" + System.currentTimeMillis()
                        .toString()
                )

                FancyToast.makeText(
                    this,
                    "Uploading Thumbnail",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.INFO,
                    false
                ).show()
                binding.progressBar2.isVisible = true

                thumbnailUploadTask = ref.putFile(result.data!!.data!!)
                thumbnailUploadTask?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        // thumbnail uploaded successfully, now download url and save it in a string
                        ref.downloadUrl
                            .addOnSuccessListener { uri ->
                                // uri downloaded successfully
                                thumbnailUrl = uri.toString()
                                FancyToast.makeText(
                                    this,
                                    "Thumbnail Upload Successfully",
                                    FancyToast.LENGTH_SHORT,
                                    FancyToast.SUCCESS,
                                    false
                                ).show()

                                binding.progressBar2.isVisible = false
                            }
                            .addOnFailureListener {
                                FancyToast.makeText(
                                    this,
                                    "Thumbnail Upload Failed",
                                    FancyToast.LENGTH_SHORT,
                                    FancyToast.ERROR,
                                    false
                                ).show()
                                ref.delete()    // since url downloading failed, so delete thumbnail from storage database as it will occupy unnecessary storage
                            }
                    } else {
                        FancyToast.makeText(
                            this,
                            "Thumbnail Upload Failed",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR,
                            false
                        ).show()
                    }
                }
            }
        }

    private fun getVideoLength(uri: Uri) {
        val ret = MediaMetadataRetriever()
        ret.setDataSource(this, uri)
        val duration =
            ret.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()
        videoLength = if (duration != null) (duration / 1000)  else  0
    }

    override fun onDestroy() {
        videoUploadTask?.cancel()    // cancel the video uploading if any video is uploading currently and activity is destroyed
        thumbnailUploadTask?.cancel()    // cancel the thumbnail uploading if any thumbnail is uploading currently and activity is destroyed
        super.onDestroy()
    }
}