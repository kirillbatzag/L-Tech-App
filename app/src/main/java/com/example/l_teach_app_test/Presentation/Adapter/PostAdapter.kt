package com.example.l_teach_app_test.Presentation.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.l_teach_app_test.Domain.Model.Post
import com.example.l_teach_app_test.databinding.NewsItemBinding
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter(private val onItemClick: (Post) -> Unit) :
    ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback()) {

    private val outputDateFormat = SimpleDateFormat("dd MMMM, HH:mm", Locale("ru"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post, outputDateFormat)
        holder.itemView.setOnClickListener {
            onItemClick(post)
        }
    }

    class PostViewHolder(private val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post, dateFormat: SimpleDateFormat) {
            binding.titleTextView.text = post.title
            binding.descriptionTextView.text = limitTextToLines(post.text)
            binding.dateTextView.text = dateFormat.format(post.date)
            
            binding.postImageView.load(post.imageUrl) {
                crossfade(true)
                //placeholder(R.drawable.ic_image_placeholder) 
                //error(R.drawable.ic_image_placeholder)       
            }
        }

        private fun limitTextToLines(text: String, maxLines: Int = 4, maxCharsPerLine: Int = 30): String {
            val maxChars = maxLines * maxCharsPerLine
            if (text.length <= maxChars) return text

            val words = text.split(" ")
            val builder = StringBuilder()
            var currentLength = 0

            for (word in words) {
                if (currentLength + word.length + 1 > maxChars) break
                builder.append(word).append(" ")
                currentLength += word.length + 1
            }

            return builder.toString().trim()
        }
    }


}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem == newItem
}