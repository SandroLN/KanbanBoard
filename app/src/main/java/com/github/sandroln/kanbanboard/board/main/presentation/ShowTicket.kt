package com.github.sandroln.kanbanboard.board.main.presentation

import com.github.sandroln.kanbanboard.board.main.data.BoardUser
import com.github.sandroln.kanbanboard.ticket.common.presentation.ColorsViewGroup
import com.github.sandroln.kanbanboard.ticket.create.presentation.AssignUser
import com.github.sandroln.kanbanboard.ticket.edit.presentation.setTextCorrect
import com.google.android.material.textfield.TextInputEditText

interface ShowTicket {

    fun show(
        titleEditText: TextInputEditText,
        colorsViewGroup: ColorsViewGroup,
        assigneeEditText: TextInputEditText,
        descriptionEditText: TextInputEditText
    )

    fun show(assignUser: AssignUser)

    data class Base(
        val colorHex: String,
        val name: String,
        val assignedMemberName: String,
        val description: String
    ) : ShowTicket {

        override fun show(
            titleEditText: TextInputEditText,
            colorsViewGroup: ColorsViewGroup,
            assigneeEditText: TextInputEditText,
            descriptionEditText: TextInputEditText
        ) {
            titleEditText.setTextCorrect(name)
            colorsViewGroup.show(colorHex)
            assigneeEditText.setTextCorrect(assignedMemberName)
            descriptionEditText.setTextCorrect(description)
        }

        override fun show(assignUser: AssignUser) =
            assignUser.assign(BoardUser.Ui(assignedMemberName))
    }
}