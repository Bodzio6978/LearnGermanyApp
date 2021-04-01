package com.gmail.Bodziowaty6978

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UsernameActivity : AppCompatActivity() {

    lateinit var database : FirebaseDatabase
    lateinit var instance : FirebaseAuth
    lateinit var userId : String

    lateinit var username : EditText
    lateinit var setUsername : Button

    lateinit var notification : TextView
    lateinit var set : Animator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username)

        database = Firebase.database("https://learn-germany-app-default-rtdb.europe-west1.firebasedatabase.app/")
        instance = FirebaseAuth.getInstance()

        userId=instance.currentUser?.uid.toString()

        username = findViewById(R.id.username_username)
        setUsername = findViewById(R.id.username_set)

        notification = findViewById(R.id.username_notification)
        set = AnimatorInflater.loadAnimator(this, R.animator.notification)
                .apply {
                    setTarget(notification)
                    addListener(onRepeat = {
                        it.pause()
                        GlobalScope.launch(Dispatchers.Main) {
                            delay(2000L)
                            it.resume()
                        }

                    })
                }

        setUsername.setOnClickListener {
            addUsername()
        }
    }

    private fun addUsername(){
        val username = username.text.toString().trim()
        if(username.isNullOrEmpty()){
            notification.text = getString(R.string.username_no_value_notification)
            set.start()
        }else if(username.length<6||username.length>24){
            notification.text = getString(R.string.username_length_notification)
            set.start()
        }else{
            checkIfUserExists()
        }
    }

    private fun checkIfUserExists(){
        val username = username.text.toString().trim()
        val refUsers = database.reference.child("users")

        refUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var doesExist = false
                for (data in snapshot.children) {
                    if (data.child("username").value.toString() == username) {
                        doesExist = true
                    }
                }
                if(doesExist){
                    notification.text = getString(R.string.username_exists_notification)
                    set.start()
                }else{
                    database.reference.child("users").child(userId).child("username").setValue(username)
                    val intent : Intent = Intent(this@UsernameActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
}