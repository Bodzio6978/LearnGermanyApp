package com.gmail.Bodziowaty6978.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gmail.Bodziowaty6978.R

class AddRecyclerAdapter(
        private var englishWords: MutableList<String>,
        private var germanWords: MutableList<String>
) :
        RecyclerView.Adapter<AddRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val englishWord: TextView = itemView.findViewById(R.id.word_english)
        val germanWord: TextView = itemView.findViewById(R.id.word_german)
        private val moreButton: ImageButton = itemView.findViewById(R.id.word_delete)

        init {
            moreButton.setOnClickListener {
                val position: Int = adapterPosition
                englishWords.removeAt(position)
                germanWords.removeAt(position)
                notifyDataSetChanged()
                Toast.makeText(it.context, "Word Has Been Deleted", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.word_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.englishWord.text = englishWords[position]
        holder.germanWord.text = germanWords[position]
    }

    override fun getItemCount(): Int {
        return germanWords.size
    }
}