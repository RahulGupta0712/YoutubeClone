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
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.notyoutube.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.shashank.sony.fancytoastlib.FancyToast
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class MainActivity : AppCompatActivity() {
    val profile_menu = ProfileMenu()
    val cast_menu = CastActivity()

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

        var fragment = FragmentHome()
        var trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.mainFrame, fragment)
        trans.commit()

        binding.bottomBar.setActiveItem(0)  // starting item index
        binding.bottomBar.setBadge(3)

        binding.bottomBar.onItemSelected = { item ->
            when (item) {
                0 -> {
                    var fragment = FragmentHome()
                    var trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.mainFrame, fragment)
                    trans.addToBackStack(null)
                    trans.commit()
                }

                1 -> {
                    var fragment = FragmentShorts()
                    var trans = supportFragmentManager.beginTransaction()
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
                    var fragment = FragmentSubscriptions()
                    var trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.mainFrame, fragment)
                    trans.addToBackStack("FragmentSubscriptions")
                    trans.commit()
                }

                4 -> {
                    var fragment = FragmentLibrary()
                    var trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.mainFrame, fragment)
                    trans.addToBackStack(null)
                    trans.commit()
                }
            }
        }

        binding.bottomBar.onItemReselected = { item ->
            when (item) {
                0 -> {
                    var fragment = FragmentHome()
                    var trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.mainFrame, fragment)
                    trans.addToBackStack(null)
                    trans.commit()
                }

                1 -> {
                    var fragment = FragmentShorts()
                    var trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.mainFrame, fragment)
                    trans.addToBackStack(null)
                    trans.commit()
                }

                2 -> {

                }

                3 -> {
                    binding.bottomBar.removeBadge(3)
                    var fragment = FragmentSubscriptions()
                    var trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.mainFrame, fragment)
                    trans.addToBackStack("FragmentSubscriptions")
                    trans.commit()
                }

                4 -> {
                    var fragment = FragmentLibrary()
                    var trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.mainFrame, fragment)
                    trans.addToBackStack(null)
                    trans.commit()
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
                startActivity(Intent(this, videoRecorder::class.java))
            }, 0)
        }
    }

    fun viewTheChannel(item: MenuItem) {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("View Your Channel")
            .setContentText("This will re-direct you to outside NotYoutube. Do You want to continue ?")
            .setConfirmButton("YES") {
                FancyToast.makeText(
                    this,
                    "Viewing Your Channel",
                    FancyToast.LENGTH_LONG,
                    FancyToast.INFO,
                    false
                ).show()
                startActivity(Intent(this, ProfileLogin::class.java))
                it.dismiss()
            }
            .setCancelButton("NO") {
                FancyToast.makeText(
                    this,
                    "Getting Back",
                    FancyToast.LENGTH_LONG,
                    FancyToast.CONFUSING,
                    false
                ).show()
                it.dismissWithAnimation()
            }
            .show()

    }

    fun logout(item: MenuItem) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Logout your Channel")
        dialog.setMessage("Are You Sure ${auth.currentUser?.email} ?")
        dialog.setIcon(R.drawable.logout_button)
        dialog.setPositiveButton("YES") { it,
                                          which ->
            auth.signOut()    // sign out the channel from app
            FancyToast.makeText(
                this,
                "Logout Successful",
                FancyToast.LENGTH_LONG,
                FancyToast.SUCCESS,
                false
            ).show()
            it.dismiss()
        }
        dialog.setNegativeButton("NO") { dialogInterface,
                                         which ->
            FancyToast.makeText(
                this,
                "Logout Unsuccessful",
                FancyToast.LENGTH_LONG,
                FancyToast.ERROR,
                false
            ).show()
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