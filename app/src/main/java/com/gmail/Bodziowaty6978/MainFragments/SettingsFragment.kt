package com.gmail.Bodziowaty6978.MainFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.gmail.Bodziowaty6978.AuthActivity
import com.gmail.Bodziowaty6978.R
import com.google.firebase.auth.FirebaseAuth


class SettingsFragment : Fragment() {

    lateinit var logout : Button

    lateinit var instance : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        instance = FirebaseAuth.getInstance()

        logout = view.findViewById(R.id.settings_logout_placeholder)

        logout.setOnClickListener {
            instance.signOut()
            val intent: Intent = Intent(activity,AuthActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return view
    }

}