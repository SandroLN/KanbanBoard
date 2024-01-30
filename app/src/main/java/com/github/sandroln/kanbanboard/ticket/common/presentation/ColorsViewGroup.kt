package com.github.sandroln.kanbanboard.ticket.common.presentation

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.view.children
import com.github.sandroln.kanbanboard.R
import com.github.sandroln.openedboard.ShowColors
import com.github.sandroln.openedboard.TicketColor

class ColorsViewGroup : FrameLayout, ShowColors {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var chosenIndex = 0
    private val colors: List<ImageButton>

    init {
        inflate(context, R.layout.colors_view_group, this)

        val colorsLayout = findViewById<LinearLayout>(R.id.colorsLinearLayout)
        colors = colorsLayout.children.map { it as ImageButton }.toList()
        colors.forEachIndexed { index, colorView ->
            val ticketColor = TicketColor.Factory.valueByIndex(index)
            colorView.setBackgroundColor(Color.parseColor(ticketColor))
            colorView.setOnClickListener {
                colors[chosenIndex].setImageResource(0)
                chosenIndex = colorsLayout.indexOfChild(colorView)
                colorView.setImageResource(R.drawable.ic_checked_48)
            }
        }
        colors[0].setImageResource(R.drawable.ic_checked_48)
    }

    fun chosenColorName() = TicketColor.Factory.nameByIndex(chosenIndex)

    fun chosenColorHex() = TicketColor.Factory.valueByIndex(chosenIndex)

    override fun show(colorHex: String) {
        colors[chosenIndex].setImageResource(0)
        chosenIndex = TicketColor.Factory.indexByValue(colorHex)
        colors[chosenIndex].setImageResource(R.drawable.ic_checked_48)
    }
}