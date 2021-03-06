package com.smarterthanmesudokuapp.ui.fragments.sudoku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarterthanmesudokuapp.R
import com.smarterthanmesudokuapp.databinding.FragmentSudokuBinding
import com.smarterthanmesudokuapp.ui.fragments.home.HomeArguments
import com.xwray.groupie.GroupieAdapter
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SudokuFragment : DaggerFragment() {

    private val historyAdapter = GroupieAdapter()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<SudokuViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FragmentSudokuBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            FragmentSudokuBinding.inflate(LayoutInflater.from(context), container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.sudokuLiveData.observe(viewLifecycleOwner) {
            viewDataBinding.sudokuHistoryRecycler.addItemDecoration(
                DividerItemDecoration(
                    viewDataBinding.sudokuHistoryRecycler.context,
                    DividerItemDecoration.VERTICAL
                ).apply {
                    ContextCompat.getDrawable(requireContext(), R.drawable.white_divider)
                        ?.let { it1 ->
                            setDrawable(
                                it1
                            )
                        }
                }
            )

            historyAdapter.updateAsync(it.map {
                SudokuHistoryItem(sudokuVo = it) {
                    findNavController().navigate(
                        R.id.action_nav_sudoku_to_nav_home,
                        Bundle().apply {
                            putParcelable(
                                "args",
                                HomeArguments(it)
                            )
                        })
                }
            })

        }
        viewDataBinding.sudokuHistoryRecycler.adapter = historyAdapter
        viewDataBinding.sudokuHistoryRecycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        viewModel.getSudokus()
    }
}