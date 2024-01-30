package com.github.sandroln.openedboard

import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText

interface ShowTicket {

    fun show(
        titleEditText: TextInputEditText,
        chooseColumnViewGroup: ChooseColumnInit,
        colorsViewGroup: ShowColors,
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
            chooseColumnViewGroup: ChooseColumnInit,
            colorsViewGroup: ShowColors,
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

interface ShowColors {
    fun show(colorHex: String)
}

interface ChooseColumnInit {
    fun init(chosenColumn: Column)
}

fun EditText.setTextCorrect(text: String) {
    setText(text)
    setSelection(text.length)
}