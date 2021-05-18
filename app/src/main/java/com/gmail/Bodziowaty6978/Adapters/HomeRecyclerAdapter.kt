package com.gmail.Bodziowaty6978.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.Bodziowaty6978.QuizActivity
import com.gmail.Bodziowaty6978.R

class HomeRecyclerAdapter(
    private var titles: MutableList<String>,
    private var authors: MutableList<String>
) :
    RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.quiz_title)
        val author: TextView = itemView.findViewById(R.id.quiz_author)
        val play: ImageButton = itemView.findViewById(R.id.quiz_play)

        init {
            play.setOnClickListener {
                val intent: Intent = Intent(itemView.context, QuizActivity::class.java)
                intent.putExtra("title",titles[adapterPosition])
                intent.putExtra("author",authors[adapterPosition])
                itemView.context.startActivity(intent)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.quiz_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = titles[position]
        holder.author.text = authors[position]
    }

    override fun getItemCount(): Int {
        return titles.size
    }


}