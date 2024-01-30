package com.github.sandroln.board.presentation

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.github.sandroln.chosenboard.BoardCache
import com.github.sandroln.chosenboard.ChosenBoardCache
import com.github.sandroln.common.NavigateToBoards
import com.github.sandroln.core.Communication
import com.github.sandroln.core.GoBack

internal class BoardToolbarViewModel(
    mapper: BoardCache.Mapper<BoardToolbarUi>,
    private val communication: BoardToolbarCommunication,
    private val navigation: BoardNavigation,
    chosenBoardCache: ChosenBoardCache.Read
) : ViewModel(), GoBack, Communication.Observe<BoardToolbarUi> {

    init {
        communication.map(chosenBoardCache.read().map(mapper))
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<BoardToolbarUi>) {
        communication.observe(owner, observer)
    }

    override fun goBack() = navigation.navigateToBoards()

    fun showSettings() = navigation.navigateToBoardSettings()
}

interface BoardNavigation : NavigateToBoards, NavigateToBoardSettings

interface NavigateToBoardSettings {
    fun navigateToBoardSettings()
}

internal class BoardToolbarMapper : BoardCache.Mapper<BoardToolbarUi> {

    override fun map(id: String, name: String, isMyBoard: Boolean, ownerId: String) =
        BoardToolbarUi(name, isMyBoard)
}

internal interface BoardToolbarCommunication : Communication.Mutable<BoardToolbarUi> {
    class Base : Communication.Regular<BoardToolbarUi>(), BoardToolbarCommunication
}

internal data class BoardToolbarUi(private val title: String, private val showSettings: Boolean) {

    fun show(titleTextView: TextView, settingsButton: View) {
        titleTextView.text = title
        settingsButton.visibility = if (showSettings) View.VISIBLE else View.GONE
    }
}