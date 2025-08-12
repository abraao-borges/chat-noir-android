package com.ifpb.chatnoir.Model

class Board(val size: Int = 11) {
    val cells = Array(size) { Array(size) { CellType.EMPTY } }
    var catPosition = Position(size / 2, size / 2)

    init {
        cells[catPosition.row][catPosition.col] = CellType.CAT
    }

    fun placeRandomFences(min: Int = 9, max: Int = 15) {
        val count = (min..max).random()
        var placed = 0
        while (placed < count) {
            val r = (0 until size).random()
            val c = (0 until size).random()
            if (cells[r][c] == CellType.EMPTY && (r != catPosition.row || c != catPosition.col)) {
                cells[r][c] = CellType.FENCE
                placed++
            }
        }
    }

    fun isEdge(pos: Position): Boolean {
        return pos.row == 0 || pos.col == 0 || pos.row == size - 1 || pos.col == size - 1
    }

    fun canMoveTo(pos: Position): Boolean {
        return pos.row in 0 until size && pos.col in 0 until size && cells[pos.row][pos.col] == CellType.EMPTY
    }
}