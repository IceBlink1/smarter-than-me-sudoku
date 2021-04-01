package com.smarterthanmesudokuapp.ui.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.smarterthanmesudokuapp.R
import com.smarterthanmesudokuapp.data.entities.Sudoku
import com.smarterthanmesudokuapp.databinding.FragmentDashboardBinding
import com.smarterthanmesudokuapp.domain.entities.SudokuVo
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class DashboardFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<DashboardViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            FragmentDashboardBinding.inflate(LayoutInflater.from(context), container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.sudokuView.setUp(
            SudokuVo(
                sudoku = listOf(
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
                solution = listOf(
                    listOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
                    listOf(2, 3, 4, 5, 6, 7, 8, 9, 1),
                    listOf(3, 4, 5, 6, 7, 8, 9, 1, 2),
                    listOf(4, 5, 6, 7, 8, 9, 1, 2, 3),
                    listOf(5, 6, 7, 8, 9, 1, 2, 3, 4),
                    listOf(6, 7, 8, 9, 1, 2, 3, 4, 5),
                    listOf(7, 8, 9, 1, 2, 3, 4, 5, 6),
                    listOf(8, 9, 1, 2, 3, 4, 5, 6, 7),
                    listOf(9, 1, 2, 3, 4, 5, 6, 7, 8, 9)
                ),
                complexity = 5
            )
        )
    }
}