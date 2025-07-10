package com.example.l_teach_app_test.Presentation.Screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs 
import coil.load
import com.example.l_teach_app_test.R
import com.example.l_teach_app_test.databinding.FragmentDetailsBinding 
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    
    private val args: DetailsFragmentArgs by navArgs()
    
    private val outputDateFormat = SimpleDateFormat("dd MMMM, HH:mm", Locale("ru"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupToolbar()
        displayPostDetails(args.post) 
    }

    private fun setupToolbar() {
        binding.toolbar.title = args.post.title
        
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp() 
        }
        
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_like -> {
                    Toast.makeText(context, "Лайк!", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_share -> {
                    Toast.makeText(context, "Поделиться!", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun displayPostDetails(post: com.example.l_teach_app_test.Domain.Model.Post) {
        binding.titleText.text = post.title
        binding.textPost.text = post.text
        binding.dateText.text = outputDateFormat.format(post.date)

        binding.imageView.load(post.imageUrl) {
            crossfade(true)
            // placeholder(R.drawable.ic_image_placeholder) 
            // error(R.drawable.ic_image_placeholder)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}