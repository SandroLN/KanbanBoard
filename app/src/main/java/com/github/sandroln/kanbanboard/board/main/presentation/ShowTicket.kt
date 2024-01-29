package com.github.sandroln.kanbanboard.board.main.presentation

import com.github.sandroln.kanbanboard.ticket.common.presentation.ChooseColumnViewGroup
import com.github.sandroln.kanbanboard.ticket.common.presentation.ColorsViewGroup
import com.github.sandroln.kanbanboard.ticket.edit.presentation.setTextCorrect
import com.github.sandroln.openedboard.AssignUser
import com.github.sandroln.openedboard.BoardUser
import com.google.android.material.textfield.TextInputEditText

interface ShowTicket {

    fun show(
        titleEditText: TextInputEditText,
        chooseColumnViewGroup: ChooseColumnViewGroup,
        colorsViewGroup: ColorsViewGroup,
        assigneeEditText: TextInputEditText,
        descriptionEditText: TextInputEditText
    )

    fun show(assignUser: AssignUser)

    data class Base(
        private val column: Column,
        private val colorHex: String,
        private val name: String,
        private val assignedMemberName: String,
        private val description: String
    ) : ShowTicket {

        override fun show(
            titleEditText: TextInputEditText,
            chooseColumnViewGroup: ChooseColumnViewGroup,
            colorsViewGroup: ColorsViewGroup,
            assigneeEditText: TextInputEditText,
            descriptionEditText: TextInputEditText
        ) {
            titleEditText.setTextCorrect(name)
            chooseColumnViewGroup.init(column)
            colorsViewGroup.show(colorHex)
            assigneeEditText.setTextCorrect(assignedMemberName)
            descriptionEditText.setTextCorrect(description)
        }

        override fun show(assignUser: AssignUser) =
            assignUser.assign(BoardUser.Ui(assignedMemberName))
    }
}