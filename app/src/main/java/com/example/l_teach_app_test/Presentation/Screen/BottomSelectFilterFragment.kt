package com.example.l_teach_app_test.Presentation.Screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.l_teach_app_test.Presentation.ViewModel.HomeViewModel
import com.example.l_teach_app_test.Presentation.ViewModel.SortOrder
import com.example.l_teach_app_test.databinding.BottomSelectFilterBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import com.example.l_teach_app_test.R
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BottomSelectFilterFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSelectFilterBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by sharedViewModel()

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSelectFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.currentSortOrder.collectLatest { sortOrder ->
                when (sortOrder) {
                    SortOrder.BY_SERVER -> binding.radioGroup.check(R.id.radioButton)
                    SortOrder.BY_DATE -> binding.radioGroup.check(R.id.radioButton2)
                }
            }
        }
        
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButton -> homeViewModel.setSortOrder(SortOrder.BY_SERVER)
                R.id.radioButton2 -> homeViewModel.setSortOrder(SortOrder.BY_DATE)
            }
            dismiss() 
        }
        
        binding.imageButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
