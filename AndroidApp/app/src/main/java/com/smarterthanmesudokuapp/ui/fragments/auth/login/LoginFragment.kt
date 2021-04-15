package com.smarterthanmesudokuapp.ui.fragments.auth.login

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.smarterthanmesudokuapp.R
import com.smarterthanmesudokuapp.databinding.FragmentLoginBinding
import com.smarterthanmesudokuapp.ui.MainActivity
import com.smarterthanmesudokuapp.ui.fragments.auth.AuthViewModel
import com.smarterthanmesudokuapp.utils.FuncUtils.navigateSafe
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class LoginFragment : DaggerFragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: AuthViewModel by viewModels({ activity as MainActivity }) { viewModelFactory }

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener {
            if (binding.loginInputEditText.text.isNullOrEmpty()
                || binding.passwordInputEditText.text.isNullOrEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Cannot log in with empty credentials",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            viewModel.login(
                binding.loginInputEditText.text.toString(),
                binding.passwordInputEditText.text.toString()
            )
        }

        binding.registerButton.setOnClickListener {
            findNavController().navigateSafe(R.id.action_navigation_login_to_navigation_register)
        }

        viewModel.loginStateLiveData.observe(viewLifecycleOwner) {
            if (it != AuthViewModel.AuthState.NOT_AUTHENTICATED) {
                findNavController().navigateSafe(R.id.action_navigation_login_to_navigation_home)
            }
        }

        binding.skipButton.setOnClickListener {
            viewModel.skipAuth()
        }

        binding.recoverPasswordButton.setOnClickListener {
            findNavController().navigateSafe(R.id.action_navigation_login_to_navigation_recover_password_email)
        }
    }

}