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
import com.gmail.Bodziowaty6978.MainActivity
import com.gmail.Bodziowaty6978.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var loginButton: Button

    lateinit var auth: FirebaseAuth

    lateinit var notification: TextView
    lateinit var set: Animator

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        email = view.findViewById(R.id.login_email)
        password = view.findViewById(R.id.login_password)
        loginButton = view.findViewById(R.id.login_button)
        notification = view.findViewById(R.id.login_notification)

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



        auth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            loginUser()
        }

        return view
    }

    private fun loginUser(){
        val emailS: String = email.text.toString().trim()
        val passwordS: String = password.text.toString().trim()

        if (emailS.isNullOrEmpty() || passwordS.isNullOrEmpty()) {
            val warning = "Please Make Sure All Fields Are Filled In Correctly."
            notification.text = warning
            set.start()
        }else {
            auth.signInWithEmailAndPassword(emailS, passwordS).addOnFailureListener {
                notification.text = it.message
                set.start()
            }.addOnCompleteListener {
                if (it.isSuccessful){
                    val intent : Intent = Intent(activity,MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }
    }
}