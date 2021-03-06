package com.android.androidprojectapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    private val settingsFragment = SettingsFragment()

    private val animationFragment = AnimationFragment()

    private val listFragment = ListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottom_navigation_menu)

        replaceFragment(listFragment)

        bottom_navigation_menu.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.home -> replaceFragment(listFragment)
                R.id.settings -> replaceFragment(settingsFragment)
                R.id.animation -> replaceFragment(animationFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if(fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.commit()
        }
    }
}