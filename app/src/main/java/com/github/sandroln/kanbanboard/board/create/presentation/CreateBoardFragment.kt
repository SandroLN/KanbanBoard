package com.github.sandroln.kanbanboard.board.create.presentation

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import com.github.sandroln.kanbanboard.R
import com.github.sandroln.kanbanboard.core.BaseFragment
import com.github.sandroln.kanbanboard.core.SimpleTextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CreateBoardFragment : BaseFragment<CreateBoardViewModel>(R.layout.fragment_create_board) {

    override val viewModelClass = CreateBoardViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<View>(R.id.backButton)
        val progressBar = view.findViewById<View>(R.id.progressBar)
        val createButton = view.findViewById<Button>(R.id.createButton)
        val textInputLayout = view.findViewById<TextInputLayout>(R.id.createBoardInputLayout)
        val textInputEditText = view.findViewById<TextInputEditText>(R.id.createBoardEditText)

        textInputEditText.addTextChangedListener(CreateBoardTextWatcher(viewModel))
        backButton.setOnClickListener {
            viewModel.goBack()
        }
        createButton.setOnClickListener {
            viewModel.createBoard(textInputEditText.text.toString())
        }
        viewModel.observe(this) {
            it.show(progressBar, createButton, textInputLayout)
        }
    }
}

private class CreateBoardTextWatcher(
    private val actions: CreateBoardUiActions
) : SimpleTextWatcher() {

    override fun afterTextChanged(s: Editable) = with(actions) {
        val text = s.toString()
        if (text.length >= minimumLength)
            checkBoard(text)
        else
            disableCreate()
    }

    companion object {
        private const val minimumLength: Int = 3
    }
}