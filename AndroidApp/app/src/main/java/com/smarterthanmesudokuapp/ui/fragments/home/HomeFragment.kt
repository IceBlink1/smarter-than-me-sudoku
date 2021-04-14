package com.smarterthanmesudokuapp.ui.fragments.home

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.smarterthanmesudokuapp.R
import com.smarterthanmesudokuapp.databinding.FragmentHomeBinding
import com.smarterthanmesudokuapp.ui.MainActivity
import com.smarterthanmesudokuapp.ui.fragments.auth.AuthViewModel
import com.smarterthanmesudokuapp.utils.FuncUtils.navigateSafe
import com.smarterthanmesudokuapp.utils.FuncUtils.observeOnce
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
            FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val observer = Observer<AuthViewModel.AuthState> {
            if (it == AuthViewModel.AuthState.NOT_AUTHENTICATED) {
                findNavController()
                    .navigateSafe(R.id.action_navigation_home_to_navigation_login)
            } else {
                (requireActivity() as MainActivity).showBottomNav()
            }
        }
        authViewModel.loginStateLiveData.observeOnce(viewLifecycleOwner, observer)
        authViewModel.refreshToken()
        if (authViewModel.loginCached() == null && authViewModel.loginStateLiveData.value != AuthViewModel.AuthState.SKIPPED) {
            findNavController().navigateSafe(R.id.action_navigation_home_to_navigation_login)
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
        viewBinding.logoutFab.setOnClickListener {
            authViewModel.logout()
            (requireActivity() as MainActivity).hideBottomNav()
            findNavController()
                .navigateSafe(R.id.action_navigation_home_to_navigation_login)
        }
    }

    override fun onDetach() {
        homeViewModel.saveSudoku(
            viewBinding.sudokuView.getSudokuVo()
        )
        super.onDetach()
    }

}