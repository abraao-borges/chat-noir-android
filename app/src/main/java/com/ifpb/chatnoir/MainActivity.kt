package com.ifpb.chatnoir

import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.gridlayout.widget.GridLayout
import com.ifpb.chatnoir.Logic.CatAI
import com.ifpb.chatnoir.Logic.GameLogic
import com.ifpb.chatnoir.Model.CellType

class MainActivity : AppCompatActivity() {
    private val game = GameLogic()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val grid = findViewById<GridLayout>(R.id.gridLayout)
        val status = findViewById<TextView>(R.id.statusText)

        grid.rowCount = game.board.size
        grid.columnCount = game.board.size

        renderBoard(grid, status)
    }

    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        ).toInt()
    }

    private fun renderBoard(grid: GridLayout, status: TextView) {
        grid.removeAllViews()

        val cellSizeDp = 35f
        val cellSizePx = dpToPx(cellSizeDp)
        val marginPx = dpToPx(0f)

        for (r in 0 until game.board.size) {
            for (c in 0 until game.board.size) {
                val btn = ImageButton(this).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = cellSizePx
                        height = cellSizePx
                        // setMargins(marginPx, marginPx, marginPx, marginPx)
                    }
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    background = null
                    adjustViewBounds = true
                }

                when (game.board.cells[r][c]) {
                    CellType.EMPTY -> btn.setImageResource(R.drawable.cell_empty)
                    CellType.FENCE -> btn.setImageResource(R.drawable.cell_fence)
                    CellType.CAT -> btn.setImageResource(R.drawable.cell_cat)
                }

                btn.setOnClickListener {
                    if (!game.gameOver && game.placeFence(r, c)) {
                        if (game.checkVictory() == null) {
                            CatAI.moveCat(game.board)
                        }
                        val result = game.checkVictory()
                        if (result != null) {
                            status.text = result
                        }
                        renderBoard(grid, status)
                    }
                }

                grid.addView(btn)
            }
        }
    }
}