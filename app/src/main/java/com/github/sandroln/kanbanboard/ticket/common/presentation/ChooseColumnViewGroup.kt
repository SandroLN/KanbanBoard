package com.github.sandroln.kanbanboard.ticket.common.presentation

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.children
import androidx.core.view.forEachIndexed
import com.github.sandroln.kanbanboard.R
import com.github.sandroln.openedboard.ChooseColumnInit
import com.github.sandroln.openedboard.Column

class ChooseColumnViewGroup : FrameLayout, ChooseColumnInit {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val columns = listOf(Column.Todo, Column.InProgress, Column.Done)

    private val columnsGroup: RadioGroup

    init {
        inflate(context, R.layout.choose_column, this)
        columnsGroup = findViewById(R.id.columnsGroup)
    }

    override fun init(chosenColumn: Column) {
        val chosenIndex = columns.indexOf(chosenColumn)
        columnsGroup.forEachIndexed { index, view ->
            (view as RadioButton).isChecked = index == chosenIndex
        }
    }

    fun chosenColumn(): Column {
        val chosenView: RadioButton =
            columnsGroup.children.find { (it as RadioButton).isChecked } as RadioButton

        val chosenIndex = columnsGroup.indexOfChild(chosenView)
        return columns[chosenIndex]
    }
}