package com.foo.individualproject.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foo.individualproject.data.model.Word
import com.foo.individualproject.databinding.LayoutWordItemBinding

class WordAdapter (
    private var words: List<Word>

): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LayoutWordItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WordViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return words.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val word = words[position]
        if(holder is WordViewHolder){
            holder.bind(word)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setWords(words: List<Word>) {
        this.words = words
        notifyDataSetChanged()
    }
    fun getWords() = words

    inner class WordViewHolder(
        private val binding: LayoutWordItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(word: Word) {
            binding.tvTitle.text = word.title
            binding.tvDetails.text = word.details
            binding.mcWord.setOnClickListener{ listener?.onClick(word) }
        }
    }

    interface Listener {
        fun onClick(word: Word)
    }

}