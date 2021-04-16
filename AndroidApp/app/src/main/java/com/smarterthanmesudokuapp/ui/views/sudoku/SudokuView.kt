package com.smarterthanmesudokuapp.ui.views.sudoku

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smarterthanmesudokuapp.R
import com.smarterthanmesudokuapp.databinding.ViewSudokuBinding
import com.smarterthanmesudokuapp.ui.entities.SudokuVo
import com.smarterthanmesudokuapp.utils.gone
import com.smarterthanmesudokuapp.utils.visible
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.OnItemClickListener
import exception.Pair

class SudokuView(context: Context, attrs: AttributeSet, defStyle: Int) : FrameLayout(
    context,
    attrs,
    defStyle
) {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    var originalField: List<MutableList<Int>> = listOf(
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    )

    var currentField: List<MutableList<Int>> = listOf(
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    )

    var solution: List<MutableList<Int>> = listOf(
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    )
    private var selectedFieldCell: Pair<SudokuCardItem, Int>? = null
    private var selectedSolutionCell: Pair<SudokuCardItem, Int>? = null
    private val fieldAdapter = GroupieAdapter()
    private val solutionAdapter = GroupieAdapter()
    private var fieldGroup: SudokuFieldGroup? = null
    var shouldEditOriginalField = true
    val binding: ViewSudokuBinding =
        ViewSudokuBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.SudokuView, defStyle, 0
        )
        a.recycle()
    }

    fun setUp(sudoku: SudokuVo) {
        originalField = sudoku.sudoku.map { it.toMutableList() }
        currentField = sudoku.currentSudoku?.map { it.toMutableList() } ?: originalField
        solution = sudoku.solution?.map { it.toMutableList() } ?: solution
        setUp()
    }

    fun setUp() {
        fieldGroup = SudokuFieldGroup(
            getSudokuVo()
        )
        fieldAdapter.clear()
        fieldAdapter.apply {
            add(
                fieldGroup!!
            )
        }

        binding.sudokuRecyclerView.adapter = fieldAdapter
        binding.sudokuRecyclerView.layoutManager = GridLayoutManager(
            context,
            SUDOKU_FIELD_WIDTH,
            RecyclerView.VERTICAL,
            false
        )
        showPicker()
        showSubmitButton()
    }

    fun showSolutionAt(position: Int) {
        currentField[position / 9][position % 9] =
            fieldGroup?.items?.get(position)?.correctValue ?: 0
        fieldGroup?.items?.get(position)?.showSolution()
    }

    fun hidePicker() {
        binding.solutionRecyclerView.gone()
    }

    fun hideAllButtons() {
        binding.submitButton.gone()
        binding.fullSolutionButton.gone()
        binding.oneStepButton.gone()
    }

    fun showPicker() {
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

    fun showSubmitButton() {
        binding.submitButton.visible()
    }

    fun showSolutionButtons() {
        binding.fullSolutionButton.visible()
        binding.fullSolutionButton.setOnClickListener {
            showSolution()
        }
        binding.oneStepButton.visible()
    }

    fun updateSolution(solution: List<MutableList<Int>>) {
        this.solution = solution
        fieldGroup?.addSolution(solution)
//        fieldAdapter.notifyDataSetChanged()
    }

    fun showSolution() {
        currentField = solution
        fieldGroup?.items?.forEach { it.showSolution() }
    }

    fun showErrorAt(pair: Pair<Int, Int>) {
        fieldGroup?.items?.get(pair.key * 9 + pair.value)?.showError()
    }


    private fun onFieldItemClickedListener() = OnItemClickListener { item, _ ->
        if (item is SudokuCardItem) {
            selectedFieldCell?.key?.setUnselected()
            selectedFieldCell = Pair(item, fieldAdapter.getAdapterPosition(item))
            selectedFieldCell?.key?.setSelected()
            selectedSolutionCell?.key?.cellValue?.let {
                if (selectedFieldCell?.key?.cellValue == it) {
                    selectedFieldCell?.key?.updateCellValue(0, shouldEditOriginalField)
                } else {
                    selectedFieldCell?.key?.updateCellValue(it, shouldEditOriginalField)
                }
                val idx = selectedFieldCell?.value
                if (idx != null) {
                    currentField[idx / 9][idx % 9] = selectedFieldCell?.key?.cellValue!!
                    if (shouldEditOriginalField) {
                        originalField[idx / 9][idx % 9] = selectedFieldCell?.key?.cellValue!!
                    }
                }
                unselectCurrentItems()
            }
        }
    }

    private fun onSolutionClickedItemListener() = OnItemClickListener { item, _ ->
        if (item is SudokuCardItem) {
            selectedSolutionCell?.key?.setUnselected()
            selectedSolutionCell = Pair(item, solutionAdapter.getAdapterPosition(item))
            selectedSolutionCell?.key?.setSelected()
            if (selectedFieldCell != null) {
                selectedSolutionCell?.key?.cellValue?.let {
                    if (selectedFieldCell?.key?.cellValue == it) {
                        selectedFieldCell?.key?.updateCellValue(0, shouldEditOriginalField)
                    } else {
                        selectedFieldCell?.key?.updateCellValue(it, shouldEditOriginalField)
                    }
                    val idx = selectedFieldCell?.value
                    if (idx != null) {
                        currentField[idx / 9][idx % 9] = selectedFieldCell?.key?.cellValue!!
                        if (shouldEditOriginalField) {
                            originalField[idx / 9][idx % 9] =
                                selectedFieldCell?.key?.cellValue!!
                        }
                    }
                    unselectCurrentItems()
                }
            }
        }
    }

    fun getSudokuVo(): SudokuVo {
        return SudokuVo(
            sudoku = originalField,
            solution = solution,
            currentSudoku = currentField,
            0
        )
    }

    private fun unselectCurrentItems() {
        selectedFieldCell?.key?.setUnselected()
        selectedSolutionCell?.key?.setUnselected()
        selectedSolutionCell = null
        selectedFieldCell = null
    }

    companion object {
        private const val SUDOKU_FIELD_WIDTH = 9
    }
}