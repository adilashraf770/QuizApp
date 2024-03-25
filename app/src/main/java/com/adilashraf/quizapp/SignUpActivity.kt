package com.adilashraf.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.adilashraf.quizapp.databinding.ActivitySignUpBinding
import com.adilashraf.quizapp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        auth = Firebase.auth
        database = Firebase.database.reference

        binding.btnSignup.setOnClickListener {
            val name = binding.editName.text.toString()
            val age = binding.editAge.text.toString().toInt()
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()

            if (name == "" || name.length <= 4) {
                Toast.makeText(this, "Please Enter Name Correctly", Toast.LENGTH_LONG).show()
            } else if (age <= 0 || age > 100) {
                Toast.makeText(this, "Please Enter Age Correctly", Toast.LENGTH_LONG).show()
            } else if (!isEmailValid(email)){
                Toast.makeText(this, "Please Enter Email Correctly", Toast.LENGTH_LONG).show()
            }else if (!isPasswordValid(password)){
                Toast.makeText(this, "Please Enter Password 1 SpecialChar,1 Digit,1 LowerCase,1 UpperCase", Toast.LENGTH_LONG).show()
            }else{
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = UserModel(name, age, email, password)
                            database.child("users")
                                .child(auth.currentUser?.uid.toString())
                                .setValue(user)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "User is Created Successfully!!", Toast.LENGTH_LONG ).show()
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                        }else
                            Toast.makeText(this, "Error creating user!!", Toast.LENGTH_LONG ).show()
                    }
            }
        }

        binding.signinTxt.setOnClickListener {
             startActivity(Intent(this, SignInActivity::class.java))

        }

    }


    // Password Validation
    private fun isPasswordValid(password: String): Boolean {
        // Define your password criteria
        val minLength = 8
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { it.isLetterOrDigit().not() }

        return password.length >= minLength
                && hasUpperCase
                && hasLowerCase
                && hasDigit
                && hasSpecialChar
    }
    // Email Validation
    private fun isEmailValid(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

}