package com.smarterthanmesudokuapp.ui.views.sudoku

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarterthanmesudokuapp.R
import com.smarterthanmesudokuapp.databinding.ViewSudokuBinding
import com.smarterthanmesudokuapp.domain.entities.SudokuVo
import com.smarterthanmesudokuapp.ui.views.utils.visible
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section

class SudokuView(context: Context, attrs: AttributeSet, defStyle: Int) : FrameLayout(
    context,
    attrs,
    defStyle
) {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    private var selectedFieldCell: SudokuCardItem? = null
    private var selectedSolutionCell: SudokuCardItem? = null
    private val fieldAdapter = GroupieAdapter()
    private val solutionAdapter = GroupieAdapter()
    private val binding: ViewSudokuBinding =
        ViewSudokuBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.SudokuView, defStyle, 0
        )
        a.recycle()
    }

    fun setUp(sudoku: SudokuVo) {

        fieldAdapter.apply {
            add(Section().apply { add(SudokuFieldGroup(sudoku)) })
        }

        binding.sudokuRecyclerView.adapter = fieldAdapter
        binding.sudokuRecyclerView.layoutManager = GridLayoutManager(
            context,
            SUDOKU_FIELD_WIDTH,
            RecyclerView.VERTICAL,
            false
        )
        if (sudoku.showSolutionGroup) {
            binding.separator.visible()
            binding.solutionRecyclerView.visible()
            fieldAdapter.setOnItemClickListener(onFieldItemClickedListener())
            solutionAdapter.add(SudokuSolutionGroup())
            solutionAdapter.setOnItemClickListener(onSolutionClickedItemListener())
            binding.solutionRecyclerView.adapter = solutionAdapter
            binding.solutionRecyclerView.layoutManager = GridLayoutManager(
                context,
                SUDOKU_FIELD_WIDTH,
                RecyclerView.VERTICAL,
                false
            )
        }
    }

    private fun onFieldItemClickedListener() = OnItemClickListener { item, _ ->
        if (item is SudokuCardItem) {
            selectedFieldCell?.setUnselected()
            selectedFieldCell = item
            selectedFieldCell?.setSelected()
            selectedSolutionCell?.cellValue?.let {
                selectedFieldCell?.updateCellValue(it)
                unselectCurrentItems()
            }
        }
    }

    private fun onSolutionClickedItemListener() = OnItemClickListener { item, _ ->
        if (item is SudokuCardItem) {
            selectedSolutionCell?.setUnselected()
            selectedSolutionCell = item
            selectedSolutionCell?.setSelected()
            if (selectedFieldCell != null) {
                selectedSolutionCell?.cellValue?.let {
                    selectedFieldCell?.updateCellValue(it)
                    unselectCurrentItems()
                }
            }
        }
    }

    private fun unselectCurrentItems() {
        selectedFieldCell?.setUnselected()
        selectedSolutionCell?.setUnselected()
        selectedSolutionCell = null
        selectedFieldCell = null
    }

    companion object {
        private const val SUDOKU_FIELD_WIDTH = 9
    }
}