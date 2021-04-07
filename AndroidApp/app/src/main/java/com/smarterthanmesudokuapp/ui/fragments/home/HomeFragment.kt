package com.smarterthanmesudokuapp.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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

    private val args: HomeArguments by navArgs()

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
            }
        }
        authViewModel.refreshToken()
        if (authViewModel.loginCached() == null) {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_login)
        }

        val sudokuVo = arguments?.getParcelable<HomeArguments>("args")
        if (sudokuVo != null) {
            viewBinding.sudokuView.setUp(sudokuVo.sudoku)
            viewBinding.sudokuView.binding.submitButton.setOnClickListener {
                viewBinding.sudokuView.binding.submitButton.gone()
                viewBinding.sudokuView.showSolutionButtons()
            }
        } else {
            viewBinding.sudokuView.setUp()
//            viewBinding.sudokuView.showPicker()
//            viewBinding.sudokuView.showSolutionButtons()

            viewBinding.sudokuView.binding.submitButton.setOnClickListener {
                viewBinding.sudokuView.binding.submitButton.gone()
                viewBinding.sudokuView.showSolutionButtons()
                viewBinding.sudokuView.shouldEditOriginalField = false
                homeViewModel.solutionLiveData.observe(viewLifecycleOwner) {
                    if (it != null) {
                        viewBinding.sudokuView.updateSolution(it.map { it.toMutableList() })
                        homeViewModel.saveSudoku(
                            viewBinding.sudokuView.getSudokuVo()
                        )
                    }
                }
                homeViewModel.getSolution(viewBinding.sudokuView.currentField)

            }
        }
    }

}