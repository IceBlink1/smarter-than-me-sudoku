package com.smarterthanmesudokuapp.ui.fragments.auth.password.code

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.smarterthanmesudokuapp.R
import com.smarterthanmesudokuapp.databinding.FragmentPasswordRecoveryBinding
import com.smarterthanmesudokuapp.ui.MainActivity
import com.smarterthanmesudokuapp.ui.fragments.auth.AuthViewModel
import com.smarterthanmesudokuapp.utils.FuncUtils.navigateSafe
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class RecoverPasswordCodeFragment : DaggerFragment() {

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
        binding.recoverPasswordButton.text = "Отправить"
        binding.recoverPasswordTextView.text =
            "Введите код, присланный на ваш email адрес"
        binding.recoverPasswordEditText.hint = "Код восстановления"

        binding.recoverPasswordButton.setOnClickListener {
            val a = binding.recoverPasswordEditText.text
            if (!a.isNullOrEmpty()) {
                viewModel.recoverPasswordCode(a.toString())
            }
            viewModel.loginStateLiveData.observe(viewLifecycleOwner) {
                if (it == AuthViewModel.AuthState.SETTING_PASSWORD) {
                    findNavController().navigateSafe(R.id.action_navigation_recover_password_code_to_navigation_recover_password_new_password)
                }
            }
        }
    }
}