package com.github.sandroln.kanbanboard.ticket.edit.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.github.sandroln.kanbanboard.R

class DeleteTicketView : LinearLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val confirmDeleteButton: View

    init {
        inflate(context, R.layout.delete_ticket, this)

        val deleteButton = findViewById<View>(R.id.deleteTicketButton)
        confirmDeleteButton = findViewById(R.id.confirmDeleteButton)
        val cancelDeleteButton = findViewById<View>(R.id.cancelDeleteButton)

        deleteButton.setOnClickListener {
            confirmDeleteButton.visibility = View.VISIBLE
            cancelDeleteButton.visibility = View.VISIBLE
            deleteButton.visibility = View.GONE
        }
        cancelDeleteButton.setOnClickListener {
            confirmDeleteButton.visibility = View.GONE
            cancelDeleteButton.visibility = View.GONE
            deleteButton.visibility = View.VISIBLE
        }
    }

    fun init(deleteTicket: DeleteTicket) = confirmDeleteButton.setOnClickListener {
        deleteTicket.deleteTicket()
    }
}