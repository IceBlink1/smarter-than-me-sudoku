package com.smarterthanmesudokuapp.ui.fragments.auth.password.email

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.smarterthanmesudokuapp.R
import com.smarterthanmesudokuapp.databinding.FragmentPasswordRecoveryBinding
import com.smarterthanmesudokuapp.ui.MainActivity
import com.smarterthanmesudokuapp.ui.fragments.auth.AuthViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class RecoverPasswordEmailFragment : DaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: AuthViewModel by viewModels({ activity as MainActivity }) { viewModelFactory }

    private lateinit var binding: FragmentPasswordRecoveryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordRecoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recoverPasswordButton.text = "Отправить письмо с кодом"
        binding.recoverPasswordEditText.hint = "Email"
        binding.recoverPasswordEditText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        binding.recoverPasswordTextView.text =
            "Введите email, привязанный к аккаунту, чтобы мы отправили письмо с кодом восстановления"

        binding.recoverPasswordButton.setOnClickListener {
            if (!binding.recoverPasswordEditText.text.isNullOrEmpty()) {
                viewModel.recoverPassword(binding.recoverPasswordEditText.text.toString())
            }
            viewModel.loginStateLiveData.observe(viewLifecycleOwner) {
                if (it == AuthViewModel.AuthState.PENDING) {
                    findNavController().navigate(R.id.action_navigation_recover_password_email_to_navigation_recover_password_code)
                }
            }
        }
    }
}