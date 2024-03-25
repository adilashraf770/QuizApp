package com.adilashraf.quizapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.adilashraf.quizapp.adapter.HistoryCoinAdapter
import com.adilashraf.quizapp.databinding.FragmentHistoryBinding
import com.adilashraf.quizapp.model.HistoryCoin
import com.adilashraf.quizapp.model.UserModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
 import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


@Suppress("DEPRECATION", "NAME_SHADOWING")
class HistoryFragment : Fragment() {
    private val binding: FragmentHistoryBinding by lazy {
        FragmentHistoryBinding.inflate(layoutInflater)
    }

    private val database: DatabaseReference =  Firebase.database.reference
    private val uid = Firebase.auth.currentUser?.uid.toString()
    private lateinit var adapter: HistoryCoinAdapter
    private val coinList = ArrayList<HistoryCoin>()

    @SuppressLint("SimpleDateFormat", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.navbarLayout.imgcoin.setOnClickListener{
            val withdrawal =  WithdrawalBottomSheet()
            withdrawal.show(requireActivity().supportFragmentManager, "show")
            withdrawal.enterTransition
        }
        binding.navbarLayout.coinTxt.setOnClickListener{
            val withdrawal =  WithdrawalBottomSheet()
            withdrawal.show(requireActivity().supportFragmentManager, "show")
            withdrawal.enterTransition
        }


        try {
            // Getting user From Database
            database.child("users").child(uid).get().addOnSuccessListener {
                if (it.exists()){
                    val data = it.getValue<UserModel>()
                    binding.navbarLayout.nameTxt.text = data!!.name
                }else{
                    Toast.makeText(requireContext(), "User Don't Exits", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener{}
            // Getting Coins From Database
            database.child("coins").child(uid).get().addOnSuccessListener {
                if (it.exists()) {
                    val data = it.value as Long
                    binding.navbarLayout.coinTxt.text = data.toString()
                }
            }.addOnFailureListener {}
            // Getting History of Coin From Database
            database.child("coinshistory").child(uid).get().addOnSuccessListener {
                 coinList.clear()
                val list = ArrayList<HistoryCoin>()
                if (it.exists()){
                    for (data in it.children){
                        val data = it.getValue<HistoryCoin>()
                        list.add(data!!)
                    }
                    list.reverse()
                    coinList.addAll(coinList)
                    adapter.notifyDataSetChanged()
                 }

            }.addOnFailureListener{}

        }catch (_: Error){}


        adapter = HistoryCoinAdapter(coinList)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return  binding.root
    }

}