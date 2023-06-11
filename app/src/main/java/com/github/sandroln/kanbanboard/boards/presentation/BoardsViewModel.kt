package com.github.sandroln.kanbanboard.boards.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.github.sandroln.kanbanboard.boards.data.BoardsRepository
import com.github.sandroln.kanbanboard.core.BaseViewModel
import com.github.sandroln.kanbanboard.core.Communication
import com.github.sandroln.kanbanboard.core.DispatchersList
import com.github.sandroln.kanbanboard.core.Init
import com.github.sandroln.kanbanboard.core.ProvideError
import com.github.sandroln.kanbanboard.core.Reload
import com.github.sandroln.kanbanboard.main.NavigationCommunication
import com.github.sandroln.kanbanboard.profile.presentation.ProfileScreen

class BoardsViewModel(
    private val navigation: NavigationCommunication.Update,
    dispatchersList: DispatchersList,
    private val boardsRepository: BoardsRepository,
    private val communication: BoardsCommunication,
) : BaseViewModel(dispatchersList), BoardsViewModelActions {

    override fun observe(owner: LifecycleOwner, observer: Observer<BoardsUiState>) =
        communication.observe(owner, observer)

    override fun init(firstRun: Boolean) {
        communication.map(BoardsUiState.Progress)
        boardsRepository.init(this)
    }

    override fun error(message: String) = communication.map(BoardsUiState.Error(message))

    override fun reload() {
        handle({ boardsRepository.data() }) {
            communication.map(BoardsUiState.Base(it))
        }
    }

    override fun retry() {
        communication.map(BoardsUiState.Progress)
        reload()
    }

    override fun openBoard(boardInfo: BoardInfo) {
        boardsRepository.save(boardInfo)
        //todo navigation.map(BoardScreen)
    }

    fun showProfile() = navigation.map(ProfileScreen)
}

interface BoardsViewModelActions : Init, Communication.Observe<BoardsUiState>, BoardClickListener,
    ReloadWithError

interface ReloadWithError : Reload, ProvideError