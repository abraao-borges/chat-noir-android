package com.ifpb.chatnoir.Logic

import com.ifpb.chatnoir.Model.Board
import com.ifpb.chatnoir.Model.CellType
import com.ifpb.chatnoir.Model.Position

class GameLogic {
    val board = Board()
    var playerScore = 0
    var cpuScore = 0
    var gameOver = false

    init {
        board.placeRandomFences()
    }

    // Novo método para reiniciar o tabuleiro
    fun restartBoard() {
        board.reset() // Chama um novo método na classe Board
        gameOver = false
    }

    fun placeFence(row: Int, col: Int): Boolean {
        if (!board.canMoveTo(Position(row, col))) return false
        board.cells[row][col] = CellType.FENCE
        return true
    }

    fun checkVictory(): String? {
        if (board.isEdge(board.catPosition)) {
            if (!gameOver) {
                cpuScore++
                gameOver = true
            }
            return "Gato venceu!"
        }

        if (CatAI.getNeighbors(board.catPosition, board.size).none { board.canMoveTo(it) }) {
            if (!gameOver) {
                playerScore++
                gameOver = true
            }
            return "Você venceu!"
        }

        return null
    }
}


