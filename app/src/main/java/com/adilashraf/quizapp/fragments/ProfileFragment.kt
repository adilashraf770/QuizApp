package com.adilashraf.quizapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.adilashraf.quizapp.R
import com.adilashraf.quizapp.SignInActivity
import com.adilashraf.quizapp.databinding.FragmentProfileBinding
import com.adilashraf.quizapp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.database.ktx.getValue

@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {
    val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    private var isExpand: Boolean = false

     private val database: DatabaseReference =  Firebase.database.reference
     private val uid = Firebase.auth.currentUser?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            database.child("users").child(uid).get().addOnSuccessListener {
                if (it.exists()){
                    val data = it.getValue<UserModel>()
                    binding.name.text =  data!!.name
                    binding.userName.text = data.name
                    binding.userAge.text = data.age.toString()
                    binding.userEmail.text = data.email
                 }else{
                    Toast.makeText(requireContext(), "User Don't Exits", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener{}
        }catch (_:Error){}


        binding.imageButton.setOnClickListener {
            if (isExpand) {
                binding.details.visibility = View.GONE
                binding.imageButton.setImageResource(R.drawable.arrowup)
            } else {
                binding.details.visibility = View.VISIBLE
                binding.imageButton.setImageResource(R.drawable.downarrow)
            }
            isExpand = !isExpand

        }
        binding.btnSignout.setOnClickListener {
            Firebase.auth.signOut()
            val i = Intent(requireContext(), SignInActivity::class.java)
            startActivity(i)
        }




    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

}