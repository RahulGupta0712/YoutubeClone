package com.example.notyoutube

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notyoutube.databinding.ActivityProfileSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.childEvents
import com.shashank.sony.fancytoastlib.FancyToast

class ProfileSignUp : AppCompatActivity() {
    private val binding by lazy{
        ActivityProfileSignUpBinding.inflate(layoutInflater)
    }

    private lateinit var auth:FirebaseAuth
    private lateinit var databaseReference:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // initialise auth
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        binding.registerButton.setOnClickListener{
            val email = binding.email.text.toString()
            val channelName = binding.channelNameSignUp.text.toString()
            val password = binding.passwordSignUp.text.toString()
            val password2 = binding.passwordSignUp2.text.toString()

            if(email.isEmpty() || channelName.isEmpty() || password.isEmpty() || password2.isEmpty()){
                FancyToast.makeText(this, "Incomplete credentials !!!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show()
            }
            else if(password != password2){
                FancyToast.makeText(this, "Password does not match !!!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show()
            }
            else{
                // register the user with email and password
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this){ task ->
                        if(task.isSuccessful){
                            FancyToast.makeText(this, "Registration Successful", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                            FancyToast.makeText(this, "Moving to Login Page", FancyToast.LENGTH_LONG, FancyToast.INFO, false).show()

                            // adding channel name to user's data
                            val currentUser = auth.currentUser
                            currentUser?.let {
                               databaseReference.child("users").child(currentUser.uid).child("Channel Name").setValue(channelName)
                               databaseReference.child("users").child(currentUser.uid).child("Username").setValue("xyz")
                            }

                            startActivity(Intent(this, ProfileLogin::class.java))
                            finish()
                        }
                        else{
                            // some exception occurred, show the error message
                            FancyToast.makeText(this, "Registration Failed", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show()
                            FancyToast.makeText(this, "${task.exception?.message}", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show()
                        }
                    }
            }
        }

        binding.signInButton.setOnClickListener{
            startActivity(Intent(this, ProfileLogin::class.java))
            finish()
        }
    }
}