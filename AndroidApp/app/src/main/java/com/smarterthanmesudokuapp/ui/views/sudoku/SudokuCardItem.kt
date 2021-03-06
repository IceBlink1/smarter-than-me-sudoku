package com.smarterthanmesudokuapp.ui.views.sudoku

import android.util.TypedValue
import android.view.View
import com.smarterthanmesudokuapp.R
import com.smarterthanmesudokuapp.databinding.ItemSudokuCardBinding
import com.xwray.groupie.viewbinding.BindableItem


class SudokuCardItem(var cellValue: Int, var correctValue: Int?, val showBorders: Boolean = false) :
    BindableItem<ItemSudokuCardBinding>() {

    private var binding: ItemSudokuCardBinding? = null
    private var blackColor: Int = -1
    private var selectColor: Int = -1
    private var twoDp: Int = -1
    private var whiteColor: Int = -1
    private var redColor: Int = -1
    private var greenColor: Int = -1

    override fun bind(viewBinding: ItemSudokuCardBinding, position: Int) {
        binding = viewBinding
        setUpCell()
    }

    fun updateCellValue(newValue: Int, isEdit: Boolean) {
        if (isEdit)
            correctValue = newValue
        if (newValue in 1..9 && newValue != cellValue) {
            cellValue = newValue
            binding?.numberTextView?.text = cellValue.toString()
            if (newValue == correctValue) {
                showNormal()
            } else {
                showError()
            }
        } else {
            cellValue = 0
            binding?.numberTextView?.text = ""
        }

        if (showBorders) {
            binding?.cellCardView?.strokeWidth = twoDp
            binding?.cellCardView?.strokeColor = blackColor
        }
        binding?.cellCardView?.invalidate()
    }

    fun setUpCell() {
        if (cellValue in 1..9) {
            binding?.numberTextView?.text = cellValue.toString()
        }
        if (showBorders) {
            binding?.cellCardView?.strokeWidth = twoDp
            binding?.cellCardView?.strokeColor = blackColor
        }
    }

    fun showSolution() {
        correctValue?.let {
            if (correctValue != 0) {

                binding?.numberTextView?.text = it.toString()
                if (correctValue == cellValue) {
                    showNormal()
                } else {
                    showCorrect()
                }
                cellValue = it
            }
            setUnselected()
        }
        binding?.numberTextView?.invalidate()
        binding?.cellCardView?.invalidate()
    }

    fun showError() {
        binding?.numberTextView?.setTextColor(redColor)
    }

    fun showNormal() {
        binding?.numberTextView?.setTextColor(blackColor)
    }

    fun showCorrect() {
        binding?.numberTextView?.setTextColor(greenColor)
    }

    fun setSelected() {
        binding?.cellCardView?.strokeWidth = twoDp
        binding?.cellCardView?.strokeColor = selectColor
        binding?.cellCardView?.invalidate()
    }

    fun setUnselected() {
        if (!showBorders) {
            binding?.cellCardView?.strokeColor = whiteColor
        } else {
            binding?.cellCardView?.strokeColor = blackColor
        }
        binding?.cellCardView?.invalidate()
    }

    override fun getLayout(): Int = R.layout.item_sudoku_card

    override fun initializeViewBinding(view: View): ItemSudokuCardBinding {
        val dip = 2f
        val r = view.context.resources
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            r?.displayMetrics
        )
        twoDp = px.toInt()
        blackColor = view.context.resources.getColor(R.color.black, null)
        selectColor = view.context.resources.getColor(R.color.colorAccent, null)
        whiteColor = view.context.resources.getColor(R.color.white, null)
        redColor = view.context.resources.getColor(R.color.red, null)
        greenColor = view.context.resources.getColor(R.color.dark_green, null)
        return ItemSudokuCardBinding.bind(view)
    }

}