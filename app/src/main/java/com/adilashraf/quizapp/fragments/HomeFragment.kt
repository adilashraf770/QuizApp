package com.adilashraf.quizapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.adilashraf.quizapp.R
import com.adilashraf.quizapp.adapter.HomeCategoryAdapter
import com.adilashraf.quizapp.databinding.FragmentHomeBinding
import com.adilashraf.quizapp.model.HomeCategory
import com.adilashraf.quizapp.model.UserModel
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    private val database: DatabaseReference =  Firebase.database.reference
    private val uid = com.google.firebase.Firebase.auth.currentUser?.uid.toString()
     private  var categoryList = ArrayList<HomeCategory>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        categoryList.add(HomeCategory(R.drawable.scince1, "Science"))
        categoryList.add(HomeCategory(R.drawable.historyimg, "SST"))
        categoryList.add(HomeCategory(R.drawable.englishs, "English"))
        categoryList.add(HomeCategory(R.drawable.mathmetic, "Mathematics"))
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        val adapter = HomeCategoryAdapter(categoryList, requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

        binding.navLayout.imgcoin.setOnClickListener{
            val withdrawal =  WithdrawalBottomSheet()
            withdrawal.show(requireActivity().supportFragmentManager, "show")
            withdrawal.enterTransition
        }
        binding.navLayout.coinTxt.setOnClickListener{
            val withdrawal =  WithdrawalBottomSheet()
            withdrawal.show(requireActivity().supportFragmentManager, "show")
            withdrawal.enterTransition
        }


        try {
            database.child("users").child(uid).get().addOnSuccessListener {
                if (it.exists()){
                    val data = it.getValue<UserModel>()
                    binding.navLayout.nameTxt.text = data!!.name.toString()
                }else{
                    Toast.makeText(requireContext(), "User Don't Exits", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener{}
        }catch (_: Error){}

        database.child("coins").child(uid).get().addOnSuccessListener {
            if (it.exists()) {
                val data = it.value as Long
                binding.navLayout.coinTxt.text = data.toString()
            }
        }.addOnFailureListener {}

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root



}