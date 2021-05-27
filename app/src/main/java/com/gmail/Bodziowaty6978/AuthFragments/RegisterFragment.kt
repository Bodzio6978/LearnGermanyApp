package com.gmail.Bodziowaty6978.AuthFragments

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.animation.addListener
import androidx.fragment.app.Fragment
import com.gmail.Bodziowaty6978.R
import com.gmail.Bodziowaty6978.UsernameActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RegisterFragment : Fragment() {
    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var confirm : EditText
    lateinit var registerButton : Button

    lateinit var auth: FirebaseAuth

    lateinit var notification: TextView
    lateinit var set: Animator


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        auth = FirebaseAuth.getInstance()

        email = view.findViewById(R.id.register_email)
        password = view.findViewById(R.id.register_password)
        confirm = view.findViewById(R.id.register_confirm)
        registerButton = view.findViewById(R.id.register_button)
        notification = view.findViewById(R.id.register_notification)

        set = AnimatorInflater.loadAnimator(context, R.animator.notification)
                .apply {
                    setTarget(notification)
                    addListener(onRepeat = {
                        it.pause()
                        GlobalScope.launch(Dispatchers.Main) {
                            delay(3000L)
                            it.resume()
                        }
                    })
                }

        registerButton.setOnClickListener {
            registerUser()
        }

        return view
    }

    private fun registerUser(){
        val emailS = email.text.toString().trim()
        val passwordS = password.text.toString().trim()
        val confirmS = confirm.text.toString().trim()
        if(emailS.isNullOrEmpty()||passwordS.isNullOrEmpty()||confirmS.isNullOrEmpty()){
            val warning = "Please Make Sure All Fields Are Filled In Correctly."
            notification.text = warning
            set.start()
        }else if(passwordS != confirmS){
            val warning = "Please Make Sure Both Passwords Are The Same."
            notification.text = warning
            set.start()
        } else{
            auth.createUserWithEmailAndPassword(emailS, passwordS).addOnFailureListener {
                notification.text = it.message
                set.start()
            }.addOnCompleteListener {
                if(it.isSuccessful){
                    val intent : Intent = Intent(activity,UsernameActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }

            }
        }
    }
}