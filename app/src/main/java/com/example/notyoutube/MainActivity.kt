package com.example.notyoutube

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.notyoutube.databinding.ActivityMainBinding
import com.shashank.sony.fancytoastlib.FancyToast

class MainActivity : AppCompatActivity() {
    val profile_menu = ProfileMenu()
    val cast_menu = CastActivity()

    private val binding:ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        var fragment = FragmentHome()
        var trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.mainFrame, fragment)
        trans.commit()

        binding.homeButton.setOnClickListener{
            var fragment = FragmentHome()
            var trans = supportFragmentManager.beginTransaction()
            trans.replace(R.id.mainFrame, fragment)
            trans.addToBackStack(null)
            trans.commit()
        }
        binding.shortsButton.setOnClickListener{
            var fragment = FragmentShorts()
            var trans = supportFragmentManager.beginTransaction()
            trans.replace(R.id.mainFrame, fragment)
            trans.addToBackStack(null)
            trans.commit()
        }
        binding.subscriptionButton.setOnClickListener{
            var fragment = FragmentSubscriptions()
            var trans = supportFragmentManager.beginTransaction()
            trans.replace(R.id.mainFrame, fragment)
            trans.addToBackStack(null)
            trans.commit()
        }
        binding.libraryButton.setOnClickListener{
            var fragment = FragmentLibrary()
            var trans = supportFragmentManager.beginTransaction()
            trans.replace(R.id.mainFrame, fragment)
            trans.addToBackStack(null)
            trans.commit()
        }


        binding.profileButton.setOnClickListener {
            profile_menu.profileMenu(this, it)
        }

        binding.castButton.setOnClickListener{
            cast_menu.castActivity(this, it)
        }

        binding.searchButton.setOnClickListener{
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, videoRecorder::class.java))
            }, 0)
        }
    }

    fun viewTheChannel(item : MenuItem){
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("View Your Channel")
            .setContentText("This will re-direct you to outside NotYoutube. Do You want to continue ?")
            .setConfirmButton("YES"){
                FancyToast.makeText(this, "Viewing Your Channel", FancyToast.LENGTH_LONG, FancyToast.INFO, false).show()
                startActivity(Intent(this, Profile::class.java))
                it.dismiss()
            }
            .setCancelButton("NO"){
                FancyToast.makeText(this, "Getting Back", FancyToast.LENGTH_LONG ,FancyToast.CONFUSING, false).show()
                it.dismissWithAnimation()
            }
            .show()

    }

    fun logout(item : MenuItem){
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("EXIT APP")
        dialog.setMessage("Are You Sure ?")
        dialog.setIcon(R.drawable.logout_button)
        dialog.setPositiveButton("YES"){it,
which-> FancyToast.makeText(this, "Logout Successful", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show()
            it.dismiss()
            finish()
        }
        dialog.setNegativeButton("NO"){dialogInterface,
                                       which-> FancyToast.makeText(this, "Logout Unsuccessful", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show()
            dialogInterface.dismiss()
        }
        val alert_dialog = dialog.create()
        alert_dialog.setCancelable(false)
        alert_dialog.show()
    }

}