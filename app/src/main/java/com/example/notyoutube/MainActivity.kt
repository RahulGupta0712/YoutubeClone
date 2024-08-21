package com.example.notyoutube

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.notyoutube.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.shashank.sony.fancytoastlib.FancyToast
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class MainActivity : AppCompatActivity() {
    private val profile_menu = ProfileMenu()
    private val cast_menu = CastActivity()

    private val requestCodeCamera = 101

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // change color of watchU
        val app = "WatchU"
        val string = SpannableString(app)
        string.setSpan(ForegroundColorSpan(Color.parseColor("#FF0000")), 0, 5, 0) // watch in red
        string.setSpan(ForegroundColorSpan(Color.parseColor("#6495ED")), 5, 6, 0)   // U is blue
        binding.watchU.text = string

        val fragment1 = FragmentHome()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainFrame, fragment1)
        transaction.commit()

        binding.bottomBar.setActiveItem(0)  // starting item index
        binding.bottomBar.setBadge(3)

        binding.bottomBar.onItemSelected = { item ->
            when (item) {
                0 -> {
                    val fragment = FragmentHome()
                    val trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.mainFrame, fragment)
                    trans.addToBackStack(null)
                    trans.commit()
                }

                1 -> {
                    val fragment = FragmentShorts()
                    val trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.mainFrame, fragment)
                    trans.addToBackStack(null)
                    trans.commit()
                }

                2 -> {
                    startActivity(Intent(this, VideoUploadActivity::class.java))
                    finish()
                }

                3 -> {
                    binding.bottomBar.removeBadge(3)
                    val fragment = FragmentSubscriptions()
                    val trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.mainFrame, fragment)
                    trans.addToBackStack("FragmentSubscriptions")
                    trans.commit()
                }

                4 -> {
                    val fragment = FragmentLibrary()
                    val trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.mainFrame, fragment)
                    trans.addToBackStack(null)
                    trans.commit()
                }
            }
        }

        binding.bottomBar.onItemReselected = { item ->
            // Refresh
            when (item) {
                0 -> {
                    val fragment = FragmentHome()
                    val trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.mainFrame, fragment)
                    trans.addToBackStack(null)
                    trans.commit()
                }

                1 -> {
                    val fragment = FragmentShorts()
                    val trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.mainFrame, fragment)
                    trans.addToBackStack(null)
                    trans.commit()
                }

                2 -> {

                }

                3 -> {
                    binding.bottomBar.removeBadge(3)
                    val fragment = FragmentSubscriptions()
                    val trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.mainFrame, fragment)
                    trans.addToBackStack("FragmentSubscriptions")
                    trans.commit()
                }

                4 -> {

                }
            }
        }


        binding.profileButton.setOnClickListener {
            profile_menu.profileMenu(this, it)
        }

        binding.castButton.setOnClickListener {
            cast_menu.castActivity(this, it)
        }

        binding.recordButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // already granted
                val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                startActivity(intent)
            } else {
                // ask for permission
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    // it is false when it is turned never ask again as true
                    // it returns true if permission was rejected previously
                    MotionToast.darkColorToast(
                        this@MainActivity,
                        "Camera Permissions",
                        "Grant the Camera permissions to start recording",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_TOP,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(
                            this,
                            www.sanju.motiontoast.R.font.helvetica_regular
                        )
                    )
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.setData(Uri.fromParts("package", packageName, null))
                    startActivity(intent)
                } else {
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.CAMERA),
                        requestCodeCamera
                    )
                }
            }

        }

        binding.searchButton.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, Search::class.java))
            }, 0)
        }
    }

    fun viewTheChannel(item: MenuItem) {
        startActivity(Intent(this, ProfileLogin::class.java))
    }

    fun logout(item: MenuItem) {
        val user = auth.currentUser
        val userInfo = if(user == null) "" else user.email

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Logout your Channel")
        dialog.setMessage("Are You Sure $userInfo")
        dialog.setIcon(R.drawable.logout_button)
        dialog.setPositiveButton("YES") { it,_ ->
            auth.signOut()    // sign out the channel from app
            FancyToast.makeText(
                this,
                "Logout Successful",
                FancyToast.LENGTH_SHORT,
                FancyToast.SUCCESS,
                false
            ).show()
            it.dismiss()
        }
        dialog.setNegativeButton("NO") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alert_dialog = dialog.create()
        alert_dialog.setCancelable(false)
        alert_dialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // now granted
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startActivity(intent)
        } else {
            MotionToast.darkColorToast(
                this@MainActivity,
                "Camera Permissions",
                "Permission Not granted, Kindly grant it",
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_TOP,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular)
            )
        }
    }

}