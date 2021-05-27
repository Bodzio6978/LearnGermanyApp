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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

lateinit var bottomNav: BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var database: FirebaseDatabase
    lateinit var instance: FirebaseAuth
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        instance = FirebaseAuth.getInstance()
        database = Firebase.database("https://learn-germany-app-default-rtdb.europe-west1.firebasedatabase.app/")


        if (instance.currentUser == null) {
            val intent: Intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            userId = instance.uid.toString()

            val ref = database.getReference("users").child(userId).child("username")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == null) {
                        val intent: Intent = Intent(this@MainActivity, UsernameActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }
            )
        }


        val home: HomeFragment = HomeFragment()
        val search: SearchFragment = SearchFragment()
        val settings: SettingsFragment = SettingsFragment()

        setFragment(home)

        bottomNav = findViewById(R.id.bottom_nav_main)

        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
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

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.apply {
            beginTransaction().replace(R.id.fl_main, fragment).commit()
        }
    }
}