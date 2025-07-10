package com.example.l_teach_app_test.Presentation.Screen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.l_teach_app_test.Presentation.ViewModel.LoginViewModel
import com.example.l_teach_app_test.R
import com.example.l_teach_app_test.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModel()

    private var isPasswordVisible = true
    private var isEnter = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hidePasswordError()

        val (savedPhone, savedPass) = viewModel.getSavedCredentials()
        binding.phoneNumberEditText.setText(savedPhone)
        binding.passwordEditText.setText(savedPass)

        updateSignInButtonState()
        setupFocusListeners(binding.phoneNumberEditText, binding.numLayout)
        setupFocusListeners(binding.passwordEditText, binding.passwordLayout)

        viewModel.loadPhoneMask()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.mask.collectLatest { mask ->
            }
        }

        binding.phoneNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateSignInButtonState() 
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.clearPhoneButton.setOnClickListener {
            binding.phoneNumberEditText.setText("")
        }

        binding.togglePasswordVisibilityButton.setOnClickListener {
            if (isPasswordVisible) {
                binding.passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.togglePasswordVisibilityButton.setImageResource(R.drawable.ic_eye_off)
                isPasswordVisible = false 
            } else {
                binding.passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.togglePasswordVisibilityButton.setImageResource(R.drawable.ic_eye) 
                isPasswordVisible = true 
            }
        }


        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateSignInButtonState() 

                hidePasswordError()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnSignIn.setOnClickListener {
            val rawPhone = binding.phoneNumberEditText.text.toString()
            val rawPassword = binding.passwordEditText.text.toString()
            
            val cleanedPhone = rawPhone.replace("[^\\d]".toRegex(), "")
            val cleanedPassword = rawPassword.trim()

            if (cleanedPhone.isEmpty() || cleanedPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            hidePasswordError()
            
            viewModel.login(cleanedPhone, cleanedPassword)
        }



        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.errorMessage.collectLatest { message ->
                message?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    viewModel.clearError()
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authError.collectLatest { message ->
                message?.let {
                    showPasswordError(it)
                    viewModel.clearError()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginSuccess.collectLatest { success ->
                if (success) {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    viewModel.clearLoginSuccess()
                    isEnter++
                } else {
                    if (isEnter == 0) {
                        showPasswordError("Неверный пароль")
                    }
                }
            }
        }
    }

    private fun setupFocusListeners(editText: EditText, container: View) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            container.isActivated = hasFocus
            if (hasFocus) {
                hidePasswordError() 
            }
        }
    }

    private fun updateSignInButtonState() {
        val phoneNumber = binding.phoneNumberEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        
        val isDataEntered = phoneNumber.length > 2 && password.isNotEmpty()
        
        binding.btnSignIn.isEnabled = isDataEntered
        
        if (isDataEntered) {
            binding.btnSignIn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
        } else {
            binding.btnSignIn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue_disabled)
            binding.btnSignIn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    private fun showPasswordError(message: String) {
        binding.passwordHelperText.text = message
        binding.passwordHelperText.visibility = View.VISIBLE
        binding.passwordLayout.isSelected = true 
    }
    
    private fun hidePasswordError() {
        binding.passwordHelperText.visibility = View.GONE
        binding.passwordLayout.isSelected = false 
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    
}