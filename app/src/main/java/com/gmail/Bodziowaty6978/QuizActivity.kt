package com.gmail.Bodziowaty6978

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.animation.addListener
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


class QuizActivity : AppCompatActivity(), View.OnClickListener {

    private var englishWords = mutableListOf<String>()
    private var germanWords = mutableListOf<String>()
    private var shuffleOrder = ArrayList<Int>()
    private var currentPosition = 0
    private var currentGermanWord = ""

    lateinit var title: String
    lateinit var author: String
    lateinit var germanWord: EditText
    lateinit var englishWord: TextView
    lateinit var check: ImageButton
    lateinit var cancel: ImageButton
    lateinit var next: ImageButton
    lateinit var wordsLeft: TextView
    lateinit var specialChars: LinearLayout
    lateinit var derDieDas: LinearLayout
    lateinit var exit : Button

    lateinit var u: Button
    lateinit var a: Button
    lateinit var o: Button
    lateinit var B: Button
    lateinit var der: Button
    lateinit var die: Button
    lateinit var das: Button
    lateinit var upperCase: SwitchCompat

    lateinit var database: FirebaseDatabase

    lateinit var set: Animator
    lateinit var notification: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        englishWord = findViewById(R.id.play_english)
        notification = findViewById(R.id.play_notification)
        germanWord = findViewById(R.id.play_german)
        check = findViewById(R.id.play_check)
        cancel = findViewById(R.id.play_cancel)
        next = findViewById(R.id.play_next)
        wordsLeft = findViewById(R.id.play_words_left)
        specialChars = findViewById(R.id.play_specialChars)
        derDieDas = findViewById(R.id.play_derDieDas)
        upperCase = findViewById(R.id.play_switch)
        exit = findViewById(R.id.play_exit)

        database = Firebase.database("https://learn-germany-app-default-rtdb.europe-west1.firebasedatabase.app/")

        title = intent.getStringExtra("title").toString()
        author = intent.getStringExtra("author").toString()

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

        val quizRef = database.reference.child("quizzes").child(author).child(title)
        quizRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                englishWords = snapshot.child("english_words").value as MutableList<String>
                germanWords = snapshot.child("german_words").value as MutableList<String>
                createShuffleOrder()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        a = findViewById(R.id.play_a)
        a.setOnClickListener(this)
        B = findViewById(R.id.play_B)
        B.setOnClickListener(this)
        o = findViewById(R.id.play_o)
        o.setOnClickListener(this)
        u = findViewById(R.id.play_u)
        u.setOnClickListener(this)
        der = findViewById(R.id.play_der)
        der.setOnClickListener(this)
        die = findViewById(R.id.play_die)
        die.setOnClickListener(this)
        das = findViewById(R.id.play_das)
        das.setOnClickListener(this)

        check.setOnClickListener {
            checkIfWordIsCorrect()
        }
        next.setOnClickListener {
            showNextWord()
        }
        exit.setOnClickListener {
            closeQuiz()
        }
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
    }

    private fun createShuffleOrder() {
        for (number in 0 until englishWords.size) {
            shuffleOrder.add(number)
        }
        shuffleOrder.shuffle()
        showUi()
    }

    private fun showUi() {
        englishWord.text = englishWords[shuffleOrder[currentPosition]]
        currentGermanWord = germanWords[shuffleOrder[currentPosition]]
        changeVisibility()
        updateWordsLeft()
    }

    private fun checkIfWordIsCorrect() {
        val wordTranslation = "${englishWord.text} - $currentGermanWord"
        englishWord.text = wordTranslation
        if (germanWord.text.toString() == currentGermanWord) {
            val textToShow = "${germanWord.text} was correct"
            notification.text = textToShow
            changeVisibility()
            germanWord.setBackgroundResource(R.drawable.edit_text_correct)
            set.start()
            shuffleOrder.removeAt(currentPosition)
        } else {
            val textToShow = "${germanWord.text} was incorrect"
            notification.text = textToShow
            changeVisibility()
            germanWord.setBackgroundResource(R.drawable.edit_text_incorrect)
            set.start()
            shuffleOrder.add(shuffleOrder.size, shuffleOrder[currentPosition])
            shuffleOrder.removeAt(currentPosition)
        }
        currentGermanWord = germanWords[currentPosition]
    }

    private fun showNextWord() {
        updateWordsLeft()
        if (shuffleOrder.size == 0) {

            val s = "Congratulations, you have finished $title "
            englishWord.text = s
            germanWord.text.clear()

            next.visibility = View.GONE
            cancel.visibility = View.GONE
            check.visibility = View.GONE
            germanWord.visibility = View.GONE
            specialChars.visibility = View.GONE
            derDieDas.visibility = View.GONE
            upperCase.visibility = View.GONE
            exit.visibility = View.VISIBLE

            var parameter = englishWord.layoutParams as RelativeLayout.LayoutParams
            parameter.setMargins(parameter.leftMargin, 150, parameter.rightMargin, parameter.bottomMargin) // left, top, right, bottom
            englishWord.layoutParams = parameter

        } else {
            englishWord.text = englishWords[shuffleOrder[currentPosition]]
            currentGermanWord = germanWords[shuffleOrder[currentPosition]]
            germanWord.setBackgroundResource(R.drawable.edit_text)
            germanWord.text.clear()
            changeVisibility()
        }
    }


    private fun changeVisibility() {
        if (check.visibility == View.GONE && next.visibility == View.GONE) {
            check.visibility = View.VISIBLE
            cancel.visibility = View.VISIBLE
        } else if (check.visibility == View.VISIBLE && next.visibility == View.GONE) {
            check.visibility = View.GONE
            cancel.visibility = View.GONE
            next.visibility = View.VISIBLE
        } else if (check.visibility == View.GONE && next.visibility == View.VISIBLE) {
            check.visibility = View.VISIBLE
            cancel.visibility = View.VISIBLE
            next.visibility = View.GONE
        }
    }

    private fun updateWordsLeft() {
        wordsLeft.text = shuffleOrder.size.toString()
    }

    private fun closeQuiz(){
        val intent : Intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.play_a -> if (!upperCase.isChecked) {
                germanWord.append("ä")
            } else {
                germanWord.append("Ä")
            }
            R.id.play_B -> if (!upperCase.isChecked) {
                germanWord.append("ß")
            } else {
                germanWord.append("ẞ")
            }
            R.id.play_o -> if (!upperCase.isChecked) {
                germanWord.append("ö")
            } else {
                germanWord.append("Ö")
            }
            R.id.play_u -> if (!upperCase.isChecked) {
                germanWord.append("ü")
            } else {
                germanWord.append("Ü")
            }
            R.id.play_der -> if (!upperCase.isChecked) {
                germanWord.append("der")
            } else {
                germanWord.append("Der")
            }
            R.id.play_die -> if (!upperCase.isChecked) {
                germanWord.append("die")
            } else {
                germanWord.append("Die")
            }
            R.id.play_das -> if (!upperCase.isChecked) {
                germanWord.append("das")
            } else {
                germanWord.append("Das")
            }


        }
    }

}