package com.smarterthanmesudokuapp.ui.fragments.auth.register

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
import com.smarterthanmesudokuapp.databinding.FragmentRegisterBinding
import com.smarterthanmesudokuapp.ui.MainActivity
import com.smarterthanmesudokuapp.ui.fragments.auth.login.LoginFragment
import com.smarterthanmesudokuapp.ui.fragments.auth.AuthViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class RegisterFragment : DaggerFragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: AuthViewModel by viewModels({ activity as MainActivity }) { viewModelFactory }

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentRegisterBinding.inflate(LayoutInflater.from(requireContext()), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener {
            if (binding.loginInputEditText.text.isNullOrEmpty()
                || binding.passwordInputEditText.text.isNullOrEmpty()
                || binding.emailInputEditText.text.isNullOrEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Cannot register with empty credentials",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            viewModel.register(
                binding.loginInputEditText.text.toString(),
                binding.passwordInputEditText.text.toString(),
                binding.emailInputEditText.text.toString()
            )
        }

        viewModel.loginLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(R.id.action_navigation_register_to_navigation_home)
            }
        }
    }

}