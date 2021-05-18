package com.gmail.Bodziowaty6978

import android.animation.Animator
import android.animation.AnimatorInflater
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.Bodziowaty6978.Adapters.AddRecyclerAdapter
import com.google.android.material.navigation.NavigationView
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

class AddActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var toolbar: Toolbar
    lateinit var drawer: DrawerLayout
    lateinit var actionBarToggle: ActionBarDrawerToggle

    lateinit var navView: NavigationView
    lateinit var recycler: RecyclerView
    lateinit var addButton: ImageButton
    lateinit var englishWordEditText: EditText
    lateinit var germanWordEditText: EditText
    lateinit var finish: ImageButton
    lateinit var quizTitle: EditText

    lateinit var notification: TextView
    lateinit var set: Animator

    private var germanWords = mutableListOf<String>()
    private var englishWords = mutableListOf<String>()

    lateinit var u: Button
    lateinit var a: Button
    lateinit var o: Button
    lateinit var B: Button
    lateinit var der: Button
    lateinit var die: Button
    lateinit var das: Button
    lateinit var upperCase: SwitchCompat

    lateinit var instance: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        toolbar = findViewById(R.id.add_toolbar)
        drawer = findViewById(R.id.add_drawer)
        navView = findViewById(R.id.navView)
        recycler = findViewById(R.id.add_recyclerView)
        addButton = findViewById(R.id.add_addButton)
        englishWordEditText = findViewById(R.id.add_english)
        germanWordEditText = findViewById(R.id.add_german)
        finish = findViewById(R.id.add_finish)
        quizTitle = findViewById(R.id.add_title)

        database = Firebase.database("https://learn-germany-app-default-rtdb.europe-west1.firebasedatabase.app/")
        instance = FirebaseAuth.getInstance()

        if (instance.currentUser != null) {
            userId = instance.currentUser?.uid.toString()
        }


        notification = findViewById(R.id.add_notification)
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


        a = findViewById(R.id.add_a)
        a.setOnClickListener(this)
        B = findViewById(R.id.add_B)
        B.setOnClickListener(this)
        o = findViewById(R.id.add_o)
        o.setOnClickListener(this)
        u = findViewById(R.id.add_u)
        u.setOnClickListener(this)
        der = findViewById(R.id.add_der)
        der.setOnClickListener(this)
        die = findViewById(R.id.add_die)
        die.setOnClickListener(this)
        das = findViewById(R.id.add_das)
        das.setOnClickListener(this)

        upperCase = findViewById(R.id.add_switch)
        upperCase.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                a.text = "Ä"
                B.text = "ẞ"
                o.text = "Ö"
                u.text = "Ü"
                der.text = getString(R.string.der_upper)
                die.text = getString(R.string.die_upper)
                das.text = getString(R.string.das_upper)
            } else {
                a.text = "ä"
                B.text = "ß"
                o.text = "ö"
                u.text = "ü"
                der.text = getString(R.string.der_lower)
                die.text = getString(R.string.die_lower)
                das.text = getString(R.string.das_lower)
            }
        }


        setSupportActionBar(toolbar)
        actionBarToggle = ActionBarDrawerToggle(this, drawer, 0, 0)
        drawer.addDrawerListener(actionBarToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarToggle.syncState()
        actionBarToggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.white)


        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = AddRecyclerAdapter(germanWords, englishWords)

        addButton.setOnClickListener {
            addWord()
        }
        finish.setOnClickListener {
            finishQuiz()

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        drawer.openDrawer(navView)
        return true
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun addWord() {
        val englishWord = englishWordEditText.text.toString().trim()
        val germanWord = germanWordEditText.text.toString().trim()

        if (englishWord.isNullOrEmpty() || germanWord.isNullOrEmpty()) {
            notification.text = getString(R.string.both_words_warning)
            set.start()
        } else {
            englishWords.add(englishWord)
            germanWords.add(germanWord)
            recycler.adapter?.notifyDataSetChanged()
            englishWordEditText.text.clear()
            germanWordEditText.text.clear()
            notification.text = getString(R.string.word_added_notification)
            set.start()
        }
    }

    private fun finishQuiz() {
        val title: String = quizTitle.text.toString().trim()
        if (title.isNullOrEmpty()) {
            notification.text = getString(R.string.quiz_title_notification)
            set.start()
        } else if (germanWords.size < 1) {
            notification.text = getString(R.string.quiz_size_notification)
            set.start()
        } else if (title.length <= 5) {
            notification.text = getString(R.string.title_length_notification)
            set.start()
        } else {
            notification.text = getString(R.string.quiz_finished)
            set.start()
            val username = database.reference.child("users").child(userId).child("username")
            username.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val quizzesRef = database.getReference("quizzes").child(snapshot.value.toString()).child(title)
                    quizzesRef.child("english_words").setValue(englishWords)
                    quizzesRef.child("german_words").setValue(germanWords)
                    database.reference.child("followed_quizzes").child(userId).child(snapshot.value.toString()).child(title).setValue(title)
                    quizTitle.text.clear()
                    germanWords.clear()
                    englishWords.clear()
                    recycler.adapter?.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add_a -> if (!upperCase.isChecked) {
                germanWordEditText.append("ä")
            } else {
                germanWordEditText.append("Ä")
            }
            R.id.add_B -> if (!upperCase.isChecked) {
                germanWordEditText.append("ß")
            } else {
                germanWordEditText.append("ẞ")
            }
            R.id.add_o -> if (!upperCase.isChecked) {
                germanWordEditText.append("ö")
            } else {
                germanWordEditText.append("Ö")
            }
            R.id.add_u -> if (!upperCase.isChecked) {
                germanWordEditText.append("ü")
            } else {
                germanWordEditText.append("Ü")
            }
            R.id.add_der -> if (!upperCase.isChecked) {
                germanWordEditText.append("der")
            } else {
                germanWordEditText.append("Der")
            }
            R.id.add_die -> if (!upperCase.isChecked) {
                germanWordEditText.append("die")
            } else {
                germanWordEditText.append("Die")
            }
            R.id.add_das -> if (!upperCase.isChecked) {
                germanWordEditText.append("das")
            } else {
                germanWordEditText.append("Das")
            }
        }
    }
}