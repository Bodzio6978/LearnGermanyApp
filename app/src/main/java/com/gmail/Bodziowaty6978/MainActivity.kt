package com.gmail.Bodziowaty6978

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gmail.Bodziowaty6978.MainFragments.HomeFragment
import com.gmail.Bodziowaty6978.MainFragments.SearchFragment
import com.gmail.Bodziowaty6978.MainFragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

lateinit var bottomNav : BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val auth = FirebaseAuth.getInstance().currentUser

        if (auth==null){
            val intent: Intent = Intent(this,AuthActivity::class.java)
            startActivity(intent)
            finish()
        }

        val home : HomeFragment = HomeFragment()
        val search : SearchFragment = SearchFragment()
        val settings : SettingsFragment = SettingsFragment()

        setFragment(home)

        bottomNav = findViewById(R.id.bottom_nav_main)

        bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> {
                    setFragment(home)
                }
                R.id.ic_search -> {
                    setFragment(search)
                }
                R.id.ic_settings -> {
                    setFragment(settings)
                }
                R.id.ic_add -> {
                    val intent = Intent(this, AddActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }
    private fun setFragment(fragment : Fragment){
        supportFragmentManager.apply {
            beginTransaction().replace(R.id.fl_main,fragment).commit()
        }
    }
}