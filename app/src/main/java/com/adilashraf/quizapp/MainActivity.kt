package com.adilashraf.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
 import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
 import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
     private lateinit var bottomNavigationView: BottomNavigationView
     private lateinit var fragmentContainerView: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigation)
        fragmentContainerView = findNavController(R.id.fragmentContainerView)
        bottomNavigationView.setupWithNavController(fragmentContainerView)

    }


}