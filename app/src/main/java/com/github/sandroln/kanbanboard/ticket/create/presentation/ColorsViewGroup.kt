package com.github.sandroln.kanbanboard.ticket.create.presentation

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.view.children
import com.github.sandroln.kanbanboard.R
import com.github.sandroln.kanbanboard.board.data.TicketColor

class ColorsViewGroup : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var chosenIndex = 0

    init {
        inflate(context, R.layout.colors_view_group, this)

        val colorsLayout = findViewById<LinearLayout>(R.id.colorsLinearLayout)
        val colors = colorsLayout.children.map { it as ImageButton }.toList()
        colors.forEachIndexed { index, colorView ->
            val ticketColor = TicketColor.Factory.list[index]
            colorView.setBackgroundColor(Color.parseColor(ticketColor.value))
            colorView.setOnClickListener {
                colors[chosenIndex].setImageResource(0)
                chosenIndex = colorsLayout.indexOfChild(colorView)
                colorView.setImageResource(R.drawable.ic_checked_48)
            }
        }
        colors[0].setImageResource(R.drawable.ic_checked_48)
    }

    fun chosenColorName() = TicketColor.Factory.list[chosenIndex].name
}