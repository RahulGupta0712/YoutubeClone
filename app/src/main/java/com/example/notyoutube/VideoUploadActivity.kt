package com.example.notyoutube

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.notyoutube.databinding.ActivityVideoUploadBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storage
import com.shashank.sony.fancytoastlib.FancyToast
import okio.Okio
import java.time.LocalDateTime
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

    // for handling cases when video/thumbnail is uploading and activity gets destroyed --- provides more robustness to application
    private var videoUploadTask: UploadTask? = null
    private var thumbnailUploadTask: UploadTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference

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
            val title = binding.titleEditTextVideo.text.toString()
            val desc = binding.DescriptionEditText.text.toString()

            if (title.isEmpty()) {
                FancyToast.makeText(this, "Title can't be empty", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
            } else if (videoUrl == null) {
                FancyToast.makeText(this, "Please upload video", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show()
            } else if (thumbnailUrl == null) {
                FancyToast.makeText(this, "Please upload thumbnail", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show()
            } else {
                // all details correct
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val type = if(videoLength <= 60) "Shorts" else "Videos"
                    val ref = databaseRef.child("users").child(currentUser.uid).child(type)
                    val key = ref.push().key
                    key?.let {
                        ref.child(key).setValue(
                            DataModelVideoDetails(
                                key,
                                title,
                                desc,
                                thumbnailUrl!!,
                                videoUrl!!,
                                videoLength, System.currentTimeMillis()
                            )
                        )
                            .addOnSuccessListener {
                                FancyToast.makeText(this, "Post successful", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener {
                                FancyToast.makeText(this, "Post Failed", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                            }
                    }
                } else {
                    FancyToast.makeText(this, "First Sign in to upload a video", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show()
                }
            }
        }

        binding.cancelPostButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private val videoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val ref = Firebase.storage.reference.child(
                    "Videos/" + UUID.randomUUID().toString() + "_" + System.currentTimeMillis()
                        .toString()
                )

                FancyToast.makeText(this, "Uploading Video", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()

                binding.progressBar.isVisible = true

                videoUploadTask = ref.putFile(result.data!!.data!!)
                videoUploadTask?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        // video uploaded successfully, save the uri in a string
                        ref.downloadUrl
                            .addOnSuccessListener { url ->
                                videoUrl = url.toString()
                                FancyToast.makeText(this, "Video Uploaded successfully", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                                getVideoLength(result.data!!.data!!)    // find video length
                                binding.progressBar.isVisible = false
                            }
                            .addOnFailureListener{
                                FancyToast.makeText(this, "Video Upload Failed", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                            }

                    } else {
                        FancyToast.makeText(this, "Video Upload Failed", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                    }
                }
            }
        }
    private val thumbnailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val ref = Firebase.storage.reference.child(
                    "Thumbnails/" + UUID.randomUUID().toString() + "_" + System.currentTimeMillis()
                        .toString()
                )

                FancyToast.makeText(this, "Uploading Thumbnail", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
                binding.progressBar2.isVisible = true

                thumbnailUploadTask = ref.putFile(result.data!!.data!!)
                    thumbnailUploadTask?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            // thumbnail uploaded successfully, now download url and save it in a string
                            ref.downloadUrl
                                .addOnSuccessListener { uri ->
                                    // uri downloaded successfully
                                    thumbnailUrl = uri.toString()
                                    FancyToast.makeText(this, "Thumbnail Upload Successfully", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()

                                    binding.progressBar2.isVisible = false
                                }
                                .addOnFailureListener {
                                    FancyToast.makeText(this, "Thumbnail Upload Failed", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                                    ref.delete()    // since url downloading failed, so delete thumbnail from storage database as it will occupy unnecessary storage
                                }
                        } else {
                            FancyToast.makeText(this, "Thumbnail Upload Failed", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                        }
                    }
            }
        }

    private fun getVideoLength(uri: Uri) {
        val ret = MediaMetadataRetriever()
        ret.setDataSource(this, uri)
        val duration =
            ret.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()
        if (duration != null) {
            videoLength = duration / 1000;
        } else videoLength = 0;
    }

    override fun onDestroy() {
        videoUploadTask?.cancel()    // cancel the video uploading if any video is uploading currently and activity is destroyed
        thumbnailUploadTask?.cancel()    // cancel the thumbnail uploading if any thumbnail is uploading currently and activity is destroyed
        super.onDestroy()
    }
}