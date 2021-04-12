package com.smarterthanmesudokuapp.ui.fragments.auth.password.newpassword

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.smarterthanmesudokuapp.R
import com.smarterthanmesudokuapp.databinding.FragmentPasswordRecoveryBinding
import com.smarterthanmesudokuapp.ui.MainActivity
import com.smarterthanmesudokuapp.ui.fragments.auth.AuthViewModel
import com.smarterthanmesudokuapp.utils.FuncUtils.navigateSafe
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class RecoverPasswordNewPasswordFragment : DaggerFragment() {


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
        binding.recoverPasswordButton.text = "Ввод"
        binding.recoverPasswordEditText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.recoverPasswordTextLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        binding.recoverPasswordTextView.text =
            "Введите новый пароль"
        binding.recoverPasswordButton.setOnClickListener {
            if (!binding.recoverPasswordEditText.text.isNullOrEmpty()) {
                viewModel.setNewPassword(binding.recoverPasswordEditText.text.toString())
            }
            viewModel.loginStateLiveData.observe(viewLifecycleOwner) {
                if (it == AuthViewModel.AuthState.AUTHENTICATED) {
                    findNavController().navigateSafe(R.id.action_navigation_recover_password_new_password_to_navigation_home)
                }
            }
        }
    }
}