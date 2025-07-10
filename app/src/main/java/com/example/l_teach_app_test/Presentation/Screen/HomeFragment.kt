package com.example.l_teach_app_test.Presentation.Screen

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.l_teach_app_test.R
import com.example.l_teach_app_test.Presentation.Adapter.PostAdapter
import com.example.l_teach_app_test.Presentation.ViewModel.HomeViewModel
import com.example.l_teach_app_test.Presentation.ViewModel.SortOrder
import com.example.l_teach_app_test.databinding.FragmentHomeBinding
import com.example.l_teach_app_test.Presentation.Decorations.CustomDividerItemDecoration
import androidx.lifecycle.lifecycleScope 
import kotlinx.coroutines.flow.collectLatest 
import kotlinx.coroutines.launch 
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by sharedViewModel()
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupToolbar()
        setupSortButton()
        observeViewModel() 
        
        if (homeViewModel.posts.value.isNullOrEmpty()) {
            homeViewModel.loadPosts()
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_refresh -> {
                    homeViewModel.loadPosts(isRefresh = true)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupSortButton() {
        binding.button2.setOnClickListener {
            val bottomSheet = BottomSelectFilterFragment()
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.currentSortOrder.collectLatest { sortOrder ->
                Log.d("HomeFragment", "Sort order observed: $sortOrder")

                binding.textView5.text = when (sortOrder) {
                    SortOrder.BY_SERVER -> "По умолчанию"
                    SortOrder.BY_DATE -> "По дате"
                }
            }
        }

    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter { post ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(post)
            findNavController().navigate(action)
        }
        binding.recyclerView.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(context)

            val dividerDrawable = ContextCompat.getDrawable(context, R.drawable.divider)
            if (dividerDrawable != null) {
                val dividerContentPaddingDp = 16f
                val dividerContentPaddingPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dividerContentPaddingDp,
                    resources.displayMetrics
                ).toInt()

                val customDivider = CustomDividerItemDecoration(dividerDrawable, dividerContentPaddingPx)
                addItemDecoration(customDivider)
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.posts.collectLatest { posts ->
                postAdapter.submitList(posts)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.errorMessage.collectLatest { error ->
                error?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    homeViewModel.clearErrorMessage()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.isLoading.collectLatest { isLoading ->
                binding.progressBar.isVisible = isLoading
                binding.recyclerView.isVisible = !isLoading
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}