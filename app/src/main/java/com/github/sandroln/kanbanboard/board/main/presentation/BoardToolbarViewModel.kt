package com.github.sandroln.kanbanboard.board.main.presentation

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.github.sandroln.kanbanboard.boards.data.ChosenBoardCache
import com.github.sandroln.kanbanboard.boards.presentation.BoardsScreen
import com.github.sandroln.kanbanboard.core.Communication
import com.github.sandroln.kanbanboard.core.GoBack
import com.github.sandroln.kanbanboard.main.NavigationCommunication

class BoardToolbarViewModel(
    private val communication: BoardToolbarCommunication,
    private val navigation: NavigationCommunication.Update,
    chosenBoardCache: ChosenBoardCache.Read
) : ViewModel(), GoBack, Communication.Observe<BoardToolbarUi> {

    init {
        chosenBoardCache.read().show(communication)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<BoardToolbarUi>) {
        communication.observe(owner, observer)
    }

    override fun goBack() = navigation.map(BoardsScreen)

    fun showSettings() = Unit//todo navigation.map(BoardSettingsScreen)
}

interface BoardToolbarCommunication : Communication.Mutable<BoardToolbarUi> {
    class Base : Communication.Regular<BoardToolbarUi>(), BoardToolbarCommunication
}

data class BoardToolbarUi(private val title: String, private val showSettings: Boolean) {

    fun show(titleTextView: TextView, settingsButton: View) {
        titleTextView.text = title
        settingsButton.visibility = if (showSettings) View.VISIBLE else View.GONE
    }
}