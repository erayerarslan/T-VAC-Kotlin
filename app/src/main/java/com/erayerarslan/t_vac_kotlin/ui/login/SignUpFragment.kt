package com.erayerarslan.t_vac_kotlin.ui.login

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.erayerarslan.t_vac_kotlin.R
import com.erayerarslan.t_vac_kotlin.core.Response
import com.erayerarslan.t_vac_kotlin.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding.buttonSignUp.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passEt.text.toString().trim()

            if (validateForm(email, password)) {
                viewModel.signUp(email, password)
            }
        }
        binding.textViewSignUp.setOnClickListener {
            findNavController().popBackStack()
        }
        lifecycleScope.launch {
            viewModel.signUpState.collect { response ->
                when (response) {
                    is Response.Loading -> {
                        Log.d("SignUpFragment", "Loading state")
                    }
                    is Response.Success -> {
                        Toast.makeText(requireContext(), "Sign-up successful", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
                    }

                    is Response.Error -> {
                        Log.d("SignUpFragment", "Error state: ${response.message}")
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        return binding.root
    }

    private fun validateForm(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.emailEt.error = "Email required"
            binding.emailEt.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEt.error = "Please enter a valid email"
            binding.emailEt.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            binding.passEt.error = "Password cannot be empty"
            binding.passEt.requestFocus()
            return false
        }

        if (password.length < 6) {
            binding.passEt.error = "Password should be at least 6 characters"
            binding.passEt.requestFocus()
            return false
        }
        if (binding.passEt.text.toString() != binding.confirmPassEt.text.toString()) {
            binding.passEt.error = "Password is not the same"
            binding.passEt.requestFocus()
            return false
        }
        if (!password.any { it.isUpperCase() }) {
            binding.passEt.error = "Şifre en az bir büyük harf içermelidir."
            binding.passEt.requestFocus()
            return false
        }
        if (!password.any { it.isDigit() }) {
            binding.passEt.error = "Şifre en az bir rakam içermelidir."
            binding.passEt.requestFocus()
            return false
        }
        val specialCharPattern = Regex("[^A-Za-z0-9]")

        if (!specialCharPattern.containsMatchIn(password)) {
            binding.passEt.error = "Şifre en az bir özel karakter içermelidir."
            binding.passEt.requestFocus()
            return false
        }

        return true
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}