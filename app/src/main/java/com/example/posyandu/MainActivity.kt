package com.example.posyandu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigation : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // inisialisasi
        bottomNavigation = findViewById(R.id.bottomnav)
        // agar fragment langsung muncul dihalaman pertama
        replaceFragment(HomeFragment())

        //fungsikan
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> replaceFragment(HomeFragment())
                R.id.settingsFragment -> replaceFragment(SettingsFragment())
                else -> {
                }
            }
            true
        }
    }
    //fungsi untuk membuat replace fragment
    //(variable : parameter )
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }

}