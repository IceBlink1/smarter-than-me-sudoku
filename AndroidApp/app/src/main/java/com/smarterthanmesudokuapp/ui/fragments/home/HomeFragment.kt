package com.smarterthanmesudokuapp.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.smarterthanmesudokuapp.R
import com.smarterthanmesudokuapp.databinding.FragmentHomeBinding
import com.smarterthanmesudokuapp.domain.entities.SudokuVo
import com.smarterthanmesudokuapp.ui.fragments.login.LoginViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class HomeFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val homeViewModel by viewModels<HomeViewModel> { viewModelFactory }

    private val loginViewModel: LoginViewModel by viewModels { viewModelFactory }

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

        val sudoku = arguments?.getParcelable<HomeArguments>("args")?.sudoku ?: listOf(
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
            listOf(2, 3, 4, 5, 6, 7, 8, 9, 1),
            listOf(3, 4, 5, 6, 7, 8, 9, 1, 2),
            listOf(4, 5, 6, 7, 8, 9, 1, 2, 3),
            listOf(5, 6, 7, 8, 9, 1, 2, 3, 4),
            listOf(6, 7, 8, 9, 1, 2, 3, 4, 5),
            listOf(7, 8, 9, 1, 2, 3, 4, 5, 6),
            listOf(8, 9, 1, 2, 3, 4, 5, 6, 7),
            listOf(9, 1, 2, 3, 4, 5, 6, 7, 8)
        )

        viewBinding.sudokuView.setUp(
            SudokuVo(
                sudoku = sudoku,
                solution = listOf(
                    listOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
                    listOf(2, 3, 4, 5, 6, 7, 8, 9, 1),
                    listOf(3, 4, 5, 6, 7, 8, 9, 1, 2),
                    listOf(4, 5, 6, 7, 8, 9, 1, 2, 3),
                    listOf(5, 6, 7, 8, 9, 1, 2, 3, 4),
                    listOf(6, 7, 8, 9, 1, 2, 3, 4, 5),
                    listOf(7, 8, 9, 1, 2, 3, 4, 5, 6),
                    listOf(8, 9, 1, 2, 3, 4, 5, 6, 7),
                    listOf(9, 1, 2, 3, 4, 5, 6, 7, 8)
                ),
                complexity = 5,
                showSolutionGroup = true
            )
        )
        if (loginViewModel.loginCached() == null) {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_login)
        }
    }

}