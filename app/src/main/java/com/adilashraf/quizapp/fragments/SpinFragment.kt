package com.adilashraf.quizapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
 import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.adilashraf.quizapp.databinding.FragmentSpinBinding
import com.adilashraf.quizapp.model.HistoryCoin
import com.adilashraf.quizapp.model.UserModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
 import java.text.SimpleDateFormat
import java.util.Date
import java.util.Random


@Suppress("DEPRECATION")
class SpinFragment : Fragment() {
    private val binding: FragmentSpinBinding by lazy {
        FragmentSpinBinding.inflate(layoutInflater)
    }

     private val database = Firebase.database.reference
    private val uid = Firebase.auth.currentUser!!.uid
    private var chance = 0L
    private var currentCoin = 0L
    val spinItems = arrayOf("100","TryAgain", "500","TryAgain", "200", "TryAgain")
     private lateinit var  timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            database.child("users").child(uid).get().addOnSuccessListener {
                if (it.exists()){
                    val data = it.getValue<UserModel>()
                    binding.navbarLayout.nameTxt.text = data!!.name
                 }else{
                    Toast.makeText(requireContext(), "User Don't Exits", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener{}

            database.child("playchance").child(uid).get().addOnSuccessListener {
                if (it.exists()) {
                    chance = it.value as Long
                    binding.chanceCount.text =  chance.toString()
                 }else{
                    binding.chanceCount.text = 0.toString()
                    binding.btnSpin.isEnabled = false
                 }
            }.addOnFailureListener {}

            database.child("coins").child(uid).get().addOnSuccessListener {
                if (it.exists()) {
                    currentCoin = it.value as Long
                    binding.navbarLayout.coinTxt.text = currentCoin.toString()
                }
            }.addOnFailureListener {}

        } catch (_: Error) {}

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

        binding.btnSpin.setOnClickListener {
            binding.btnSpin.isEnabled = false

            if (chance > 0) {
                Toast.makeText(requireContext(), "Spin button in clicked", Toast.LENGTH_LONG).show()

                val spin = Random().nextInt(6)
                val degree = 60f * spin
                timer = object : CountDownTimer(5000, 50) {
                    var rotation = 0f
                    override fun onTick(millisUntilFinished: Long) {
                        rotation += 5f
                        if (rotation >= degree) {
                            rotation = degree
                            timer.cancel()
                            showResult(spinItems[spin], spin)
                        }
                        binding.wheel.rotation = rotation
                    }
                    override fun onFinish() {}
                }.start()

            }else{
                binding.btnSpin.isEnabled =  false
                Toast.makeText( requireContext(), "Play Quiz to get a Chance", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View = binding.root

     @SuppressLint("SetTextI18n", "SimpleDateFormat")
     private fun showResult(spinTxt: String, spinItemsIndex: Int) {
        if (spinItemsIndex % 2 == 0 ){
            val coin = spinTxt.toInt()

            try {

                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                val currentDate = sdf.format(Date())
                database.child("coins").child(uid).setValue(coin+currentCoin)
                binding.navbarLayout.coinTxt.text = (coin+currentCoin).toString()
                val history = HistoryCoin(currentDate, coin.toString())
                database.child("coinshistory").child(uid).push().setValue(history)

            }catch (_: Error){}
            Toast.makeText(requireContext(), spinTxt, Toast.LENGTH_LONG).show()
        }

        chance--
        try {
            database.child("playchance").child(uid).setValue(chance)
        }catch (_: Error){}

        binding.chanceCount.text =  chance.toString()
        binding.btnSpin.isEnabled = true
    }

}