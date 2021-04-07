package com.smarterthanmesudokuapp.ui.views.sudoku

import com.smarterthanmesudokuapp.domain.entities.SudokuVo
import com.xwray.groupie.Group
import com.xwray.groupie.GroupDataObserver
import com.xwray.groupie.Item

class SudokuFieldGroup(val sudokuVo: SudokuVo) : Group {

    val items: List<SudokuCardItem> =
        sudokuVo.solution?.let {
            sudokuVo.sudoku.flatten().zip(it.flatten()).map {
                SudokuCardItem(it.first, it.second)
            }
        } ?: listOf()

    override fun getItemCount(): Int {
        return FIELD_SIZE
    }

    override fun getItem(position: Int): Item<*> {
        return items[position]
    }

    override fun getPosition(item: Item<*>): Int {
        return items.indexOf(item)
    }

    override fun registerGroupDataObserver(groupDataObserver: GroupDataObserver) {
        // no-op
    }

    override fun unregisterGroupDataObserver(groupDataObserver: GroupDataObserver) {
        // no-op
    }

    companion object {
        private const val FIELD_SIZE = 81
    }
}