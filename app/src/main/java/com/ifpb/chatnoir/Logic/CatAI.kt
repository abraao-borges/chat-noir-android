package com.ifpb.chatnoir.Logic

import com.ifpb.chatnoir.Model.Board
import com.ifpb.chatnoir.Model.Position

object CatAI {

    fun getNeighbors(pos: Position, size: Int): List<Position> {
        val (r, c) = pos
        return if (r % 2 == 0) {
            listOf(
                Position(r - 1, c - 1),
                Position(r - 1, c),
                Position(r, c - 1),
                Position(r, c + 1),
                Position(r + 1, c - 1),
                Position(r + 1, c)
            ).filter { it.row in 0 until size && it.col in 0 until size }
        } else {
            listOf(
                Position(r - 1, c),
                Position(r - 1, c + 1),
                Position(r, c - 1),
                Position(r, c + 1),
                Position(r + 1, c),
                Position(r + 1, c + 1)
            ).filter { it.row in 0 until size && it.col in 0 until size }
        }
    }

    private fun distanceToEdge(pos: Position, size: Int): Int {
        val distances = listOf(pos.row, pos.col, size - 1 - pos.row, size - 1 - pos.col)
        return distances.minOrNull() ?: 0
    }

    fun moveCat(board: Board): Boolean {
        val neighbors = getNeighbors(board.catPosition, board.size)
            .filter { board.canMoveTo(it) }

        if (neighbors.isEmpty()) {
            // Gato preso, CPU perdeu
            return false
        }

        val nextPos = neighbors.minByOrNull { distanceToEdge(it, board.size) } ?: neighbors.first()

        // Atualiza posição
        board.cells[board.catPosition.row][board.catPosition.col] = com.ifpb.chatnoir.Model.CellType.EMPTY
        board.catPosition = nextPos
        board.cells[nextPos.row][nextPos.col] = com.ifpb.chatnoir.Model.CellType.CAT

        return !board.isEdge(nextPos) // false se gato escapou
    }
}