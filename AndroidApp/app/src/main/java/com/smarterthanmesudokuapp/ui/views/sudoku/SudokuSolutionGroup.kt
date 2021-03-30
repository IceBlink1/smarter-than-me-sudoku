package com.smarterthanmesudokuapp.ui.views.sudoku

import com.xwray.groupie.Group
import com.xwray.groupie.GroupDataObserver
import com.xwray.groupie.Item

class SudokuSolutionGroup : Group {

    val items = (1..9).map { SudokuCardItem(cellValue = it, correctValue = it, showBorders = true) }

    override fun getItemCount(): Int {
        return SUDOKU_NUMBERS_COUNT
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
        private const val SUDOKU_NUMBERS_COUNT = 9
    }
}