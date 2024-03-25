package com.adilashraf.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
 import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.adilashraf.quizapp.databinding.ActivitySignInBinding
 import com.google.firebase.auth.FirebaseAuth
 import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
         auth = Firebase.auth
        binding.btnSignin.setOnClickListener{

            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                          Toast.makeText(this, "User is Sign in Successfully!!", Toast.LENGTH_LONG ).show()
                           val intent = Intent(this, MainActivity::class.java)
                           startActivity(intent)
                    }else
                        Toast.makeText(this, "Error Signing In user", Toast.LENGTH_LONG ).show()
                }

        }

        binding.signupTxt.setOnClickListener{
            val i = Intent(this@SignInActivity, SignUpActivity::class.java )
            startActivity(i)
        }


    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser?.uid != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }




}