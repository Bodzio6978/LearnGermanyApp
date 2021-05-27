package com.gmail.Bodziowaty6978.MainFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.Bodziowaty6978.Adapters.HomeRecyclerAdapter
import com.gmail.Bodziowaty6978.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    lateinit var recycler: RecyclerView

    lateinit var database: FirebaseDatabase
    lateinit var instance: FirebaseAuth
    lateinit var userId: String

    private var titles = mutableListOf<String>()
    private var authors = mutableListOf<String>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        instance = FirebaseAuth.getInstance()
        userId = instance.currentUser?.uid.toString()
        database = Firebase.database("https://learn-germany-app-default-rtdb.europe-west1.firebasedatabase.app/")

        recycler = view.findViewById(R.id.home_rv)
        recycler.layoutManager = LinearLayoutManager(view.context)
        recycler.adapter = HomeRecyclerAdapter(titles, authors)

        if (authors.isEmpty()) {
            addQuizzes()
        }
        return view
    }

    private fun addQuizzes() {
        val followedRef = database.reference.child("followed_quizzes").child(userId)
        followedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                authors.clear()
                titles.clear()
                for (data in snapshot.children) {
                    for (quiz in data.children) {
                        authors.add(data.key.toString())
                        titles.add(quiz.value.toString())
                    }
                }
                recycler.adapter?.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

}