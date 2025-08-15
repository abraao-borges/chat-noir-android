package com.ifpb.chatnoir

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.gridlayout.widget.GridLayout
import com.ifpb.chatnoir.Logic.CatAI
import com.ifpb.chatnoir.Logic.GameLogic
import com.ifpb.chatnoir.Model.CellType

class MainActivity : AppCompatActivity() {
    private lateinit var game: GameLogic
    private lateinit var grid: GridLayout
    private lateinit var statusText: TextView
    private lateinit var playerScoreText: TextView
    private lateinit var cpuScoreText: TextView
     private lateinit var restartButton: Button
    private lateinit var continueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val startButton: Button = findViewById(R.id.startButton)
        startButton.setOnClickListener {
            showGameScreen()
        }
    }

    private fun showGameScreen() {
        setContentView(R.layout.activity_main)
        initializeGameComponents()

        game = GameLogic()
        startGame()
    }

    private fun initializeGameComponents() {
        grid = findViewById(R.id.gridLayout)
        statusText = findViewById(R.id.statusText)
        playerScoreText = findViewById(R.id.playerScoreText)
        cpuScoreText = findViewById(R.id.cpuScoreText)

        restartButton = findViewById(R.id.restartButton)
        continueButton = findViewById(R.id.continueButton)


        restartButton.setOnClickListener {
            game = GameLogic()
            startGame()
        }

        continueButton.setOnClickListener {
            game.restartBoard()
            startGame()
        }
    }

    private fun startGame() {
        statusText.text = "Prenda o gato!"
        restartButton.visibility = View.GONE
        continueButton.visibility = View.GONE
        grid.rowCount = game.board.size
        grid.columnCount = game.board.size
        renderBoard()
        updateScores()
    }

    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        ).toInt()
    }

    private fun renderBoard() {
        grid.removeAllViews()

        val cellSizeDp = 40f
        val cellSizePx = dpToPx(cellSizeDp)

        val negativeMarginDp = -5f
        val negativeMarginPx = dpToPx(negativeMarginDp)

        for (r in 0 until game.board.size) {
            for (c in 0 until game.board.size) {
                val btn = ImageButton(this).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = cellSizePx
                        height = cellSizePx

                        setMargins(negativeMarginPx, negativeMarginPx, negativeMarginPx, negativeMarginPx)
                    }
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    background = null
                    adjustViewBounds = true
                }

                when (game.board.cells[r][c]) {
                    CellType.EMPTY -> btn.setImageResource(R.drawable.cell_empty)
                    CellType.FENCE -> btn.setImageResource(R.drawable.bomba)
                    CellType.CAT -> btn.setImageResource(R.drawable.gato)
                }

                btn.setOnClickListener {
                    if (!game.gameOver && game.placeFence(r, c)) {
                        CatAI.moveCat(game.board)

                        val result = game.checkVictory()
                        if (result != null) {
                            statusText.text = result
                            restartButton.visibility = View.VISIBLE
                            continueButton.visibility = View.VISIBLE
                        }
                        renderBoard()
                        updateScores()
                    }
                }
                grid.addView(btn)
            }
        }
    }

    private fun updateScores() {
        playerScoreText.text = "Jogador: ${game.playerScore}"
        cpuScoreText.text = "Gato: ${game.cpuScore}"
    }
}


