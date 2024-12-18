package com.erayerarslan.t_vac_kotlin

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.erayerarslan.t_vac_kotlin.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        // BottomNavigationView ile NavController'ı bağla
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.searchFragment -> {
                    navController.navigate(R.id.searchFragment)
                    true
                }


                else -> false
            }
        }


    }






    private fun createBottomNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNavigation,navHostFragment.navController)


    }


    fun showBottomNavigationView() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    fun hideBottomNavigationView() {
        binding.bottomNavigation.visibility = View.GONE
    }




    override fun onSupportNavigateUp(): Boolean {

        return  navController.navigateUp() ||   super.onSupportNavigateUp()
    }
}