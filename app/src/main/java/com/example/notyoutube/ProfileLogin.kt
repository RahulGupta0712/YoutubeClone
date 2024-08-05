package com.example.notyoutube

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notyoutube.databinding.ActivityProfileLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.shashank.sony.fancytoastlib.FancyToast

class ProfileLogin : AppCompatActivity() {
    private val binding by lazy{
        ActivityProfileLoginBinding.inflate(layoutInflater)
    }

    private lateinit var auth:FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
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

        binding.signInButton.setOnClickListener{
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if(email.isEmpty() || password.isEmpty()) {
                FancyToast.makeText(
                    this,
                    "Incomplete credentials",
                    FancyToast.LENGTH_LONG,
                    FancyToast.ERROR,
                    false
                ).show()
            }
            else{
                // login the user with this email and password
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this){task ->
                        if(task.isSuccessful){
                            FancyToast.makeText(this, "Login Successful", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show()
                            startActivity(Intent(this, Profile::class.java))
                            finish()
                        }
                        else{
                            FancyToast.makeText(this, "Login Failed", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                            FancyToast.makeText(this, "${task.exception?.message}", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                        }
                    }

            }
        }

        binding.signUpButton.setOnClickListener{
            startActivity(Intent(this, ProfileSignUp::class.java))
            finish()
        }

        binding.googleSignIn.setOnClickListener{
            // login the user using google
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            googleSignInClient.signOut()
            launcher.launch(googleSignInClient.signInIntent)
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            if(task.isSuccessful){
                val account = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this){ res->
                        if(res.isSuccessful){
                            FancyToast.makeText(this, "Google Sign-In Successful", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                            startActivity(Intent(this, Profile::class.java))
                            finish()
                        }
                        else{
                            FancyToast.makeText(this, "Google Sign-In Failed", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                            FancyToast.makeText(this, "${res.exception?.message}", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                        }
                    }
            }
            else{
                FancyToast.makeText(this, "Google Sign-In Failed", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                FancyToast.makeText(this, "${task.exception?.message}", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
            }
        }
        else{
            FancyToast.makeText(this, "Google Sign-In Failed", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
        }
    }

    override fun onStart() {
        super.onStart()

        if(auth.currentUser != null){
            startActivity(Intent(this, Profile::class.java))
            finish()
        }
    }
}