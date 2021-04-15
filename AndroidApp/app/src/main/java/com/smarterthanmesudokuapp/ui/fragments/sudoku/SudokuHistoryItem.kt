package com.smarterthanmesudokuapp.ui.fragments.sudoku

import android.view.View
import com.smarterthanmesudokuapp.R
import com.smarterthanmesudokuapp.databinding.ItemSudokuHistoryBinding
import com.smarterthanmesudokuapp.ui.entities.SudokuVo
import com.smarterthanmesudokuapp.utils.gone
import com.xwray.groupie.viewbinding.BindableItem

class SudokuHistoryItem(private val sudokuVo: SudokuVo, val onClickCallback: (SudokuVo) -> Unit) :
    BindableItem<ItemSudokuHistoryBinding>() {

    private var binding: ItemSudokuHistoryBinding? = null

    override fun getLayout(): Int {
        return R.layout.item_sudoku_history
    }

    override fun bind(viewBinding: ItemSudokuHistoryBinding, position: Int) {
        binding = viewBinding
        setUp()
    }

    private fun setUp() {
        binding?.historySudokuView?.setUp(sudokuVo)
        binding?.historySudokuView?.hideAllButtons()
        binding?.historySudokuView?.hidePicker()
        binding?.historySudokuView?.binding?.separator?.gone()
        binding?.complexityRatingBar?.rating =
            if (sudokuVo.complexity?.toFloat() == 0f) sudokuVo.complexity.toFloat() else 5f
        binding?.complexityRatingBar?.invalidate()
    }

    override fun initializeViewBinding(view: View): ItemSudokuHistoryBinding {
        view.setOnClickListener {
            onClickCallback(sudokuVo)
        }
        return ItemSudokuHistoryBinding.bind(view)
    }
}