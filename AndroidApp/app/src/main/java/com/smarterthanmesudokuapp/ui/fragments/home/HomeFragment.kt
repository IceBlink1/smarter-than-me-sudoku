package com.smarterthanmesudokuapp.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.smarterthanmesudokuapp.R
import com.smarterthanmesudokuapp.databinding.FragmentHomeBinding
import com.smarterthanmesudokuapp.domain.entities.SudokuVo
import com.smarterthanmesudokuapp.ui.MainActivity
import com.smarterthanmesudokuapp.ui.fragments.auth.AuthViewModel
import com.smarterthanmesudokuapp.utils.gone
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class HomeFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val homeViewModel by viewModels<HomeViewModel> { viewModelFactory }

    private val authViewModel: AuthViewModel by viewModels({ activity as MainActivity }) { viewModelFactory }

    private lateinit var viewBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding =
            FragmentHomeBinding.inflate(LayoutInflater.from(requireContext()), container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.loginStateLiveData.observe(viewLifecycleOwner) {
            if (it == AuthViewModel.AuthState.NOT_AUTHENTICATED) {
                findNavController()
                    .navigate(R.id.action_navigation_home_to_navigation_login)
            } else {
                (requireActivity() as MainActivity).showBottomNav()
            }
        }
        authViewModel.refreshToken()
        if (authViewModel.loginCached() == null && authViewModel.loginStateLiveData.value != AuthViewModel.AuthState.SKIPPED) {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_login)
        }

        val sudokuVo = arguments?.getParcelable<HomeArguments>("args")
        if (sudokuVo != null && sudokuVo.sudoku.solution != null) {
            viewBinding.sudokuView.setUp(sudokuVo.sudoku)
            viewBinding.sudokuView.binding.submitButton.setOnClickListener {
                viewBinding.sudokuView.binding.submitButton.gone()
                viewBinding.sudokuView.showSolutionButtons()
            }
        } else {
            if (sudokuVo?.sudoku != null) {
                viewBinding.sudokuView.setUp(sudokuVo.sudoku)
            } else {
                viewBinding.sudokuView.setUp()
            }
            bindProgressButton(viewBinding.sudokuView.binding.submitButton)
            viewBinding.sudokuView.binding.submitButton.attachTextChangeAnimator()
            viewBinding.sudokuView.binding.submitButton.setOnClickListener {
                (it as Button).showProgress()
                homeViewModel.solutionLiveData.observe(viewLifecycleOwner) {
                    if (it != null) {
                        viewBinding.sudokuView.binding.submitButton.gone()
                        viewBinding.sudokuView.showSolutionButtons()
                        viewBinding.sudokuView.shouldEditOriginalField = false
                        viewBinding.sudokuView.updateSolution(it.map { it.toMutableList() })
                        homeViewModel.saveSudoku(
                            viewBinding.sudokuView.getSudokuVo()
                        )
                        viewBinding.sudokuView.binding.oneStepButton.setOnClickListener {
                            homeViewModel.getNextStep(viewBinding.sudokuView.currentField)
                            homeViewModel.stepLiveData.observe(viewLifecycleOwner) {
                                viewBinding.sudokuView.showSolutionAt(it)
                            }
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Введенный судоку не верен",
                            Toast.LENGTH_LONG
                        ).show()
                        viewBinding.sudokuView.binding.submitButton.hideProgress(R.string.finish_edit)
                    }
                }
                homeViewModel.getSolution(viewBinding.sudokuView.currentField)

            }

        }
    }

}