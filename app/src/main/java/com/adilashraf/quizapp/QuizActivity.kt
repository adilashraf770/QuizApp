package com.adilashraf.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.adilashraf.quizapp.databinding.ActivityQuizBinding
import com.adilashraf.quizapp.fragments.WithdrawalBottomSheet
import com.adilashraf.quizapp.model.QuestionModel
import com.adilashraf.quizapp.model.UserModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION")
class QuizActivity : AppCompatActivity() {

    private val binding: ActivityQuizBinding by lazy {
        ActivityQuizBinding.inflate(layoutInflater)
    }

    private var currentQuestion = 0
    private var score = 0
    private var currentChance = 0L

    private val uid = Firebase.auth.currentUser!!.uid
    private val questionList = ArrayList<QuestionModel>()
    private val firestore = Firebase.firestore
    private val database = Firebase.database.reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.navbarLayout.imgcoin.setOnClickListener{
            val withdrawal =  WithdrawalBottomSheet()
            withdrawal.show(this.supportFragmentManager, "show")
            withdrawal.enterTransition
        }
        binding.navbarLayout.coinTxt.setOnClickListener{
            val withdrawal =  WithdrawalBottomSheet()
            withdrawal.show(this.supportFragmentManager, "show")
            withdrawal.enterTransition
        }

        val image = intent.getIntExtra("categoryImg", 0)
        val title = intent.getStringExtra("categoryTitle").toString()


        // Geting Questions from Firestore Database
        firestore.collection("Questions").document(title).collection("questions").get()
            .addOnSuccessListener { questions ->
                questionList.clear()
                for (q in questions) {
                    val question: QuestionModel = q!!.toObject(QuestionModel::class.java)
                    questionList.add(question)
                }

                if (currentQuestion < questionList.size ){
                    binding.questionTitle.text = questionList[currentQuestion].question
                    binding.option1.text = questionList[currentQuestion].option1
                    binding.option2.text = questionList[currentQuestion].option2
                    binding.option3.text = questionList[currentQuestion].option3
                    binding.option4.text = questionList[currentQuestion].option4
                }

            }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents: ", exception)
            }

        binding.quiztitle.text = title
        binding.quizImage.setImageResource(image)

        binding.option1.setOnClickListener {
            nextQuestionAndUpdate(binding.option1.text.toString())
        }

        binding.option2.setOnClickListener{
            nextQuestionAndUpdate(binding.option2.text.toString())
        }

        binding.option3.setOnClickListener{
            nextQuestionAndUpdate(binding.option3.text.toString())
        }

        binding.option4.setOnClickListener{
            nextQuestionAndUpdate(binding.option4.text.toString())
        }


            try {
                // Geting Users from Database
                database.child("users").child(uid).get().addOnSuccessListener {
                        if (it.exists()){
                            val data = it.getValue<UserModel>()
                            binding.navbarLayout.nameTxt.text = data!!.name
                        }else{
                            Toast.makeText(this, "User Don't Exits", Toast.LENGTH_LONG).show()
                        }
                    }.addOnFailureListener{}
                // Geting Player Chances from Database
                database.child("playchance").child(uid).get().addOnSuccessListener {
                        if (it.exists()) {
                            val data = it.value as Long
                            currentChance = data
                         }
                    }.addOnFailureListener {}
                // Geting Player Coin from Database
                database.child("coins").child(uid).get().addOnSuccessListener {
                    if (it.exists()) {
                        val data = it.value as Long
                        binding.navbarLayout.coinTxt.text = data.toString()
                    }
                }.addOnFailureListener {}
            } catch (_: Error) {}


    }

    private fun nextQuestionAndUpdate(s: String) {

        if (questionList[currentQuestion].answer == s){
            score+= 10
            Toast.makeText(this@QuizActivity, "Correct Answer", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this@QuizActivity, "Wrong Answer ", Toast.LENGTH_LONG).show()
        }

        currentQuestion++

        if (currentQuestion >= questionList.size){

            if (score >= 30 ){
                binding.winner.visibility = View.VISIBLE
                binding.quizlayout.visibility = View.GONE
                // Seting Player Chances In Database
                try {
                    database.child("playchance").child(uid).setValue(currentChance+1)
                }catch (_: Error){}

            }else{
                binding.loser.visibility = View.VISIBLE
                binding.quizlayout.visibility = View.GONE
            }

        }else {
            binding.questionTitle.text = questionList[currentQuestion].question
            binding.option1.text = questionList[currentQuestion].option1
            binding.option2.text = questionList[currentQuestion].option2
            binding.option3.text = questionList[currentQuestion].option3
            binding.option4.text = questionList[currentQuestion].option4
        }

    }
}