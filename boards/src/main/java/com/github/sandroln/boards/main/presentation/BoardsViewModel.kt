package com.github.sandroln.boards.main.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.github.sandroln.boards.main.data.BoardsRepository
import com.github.sandroln.chosenboard.BoardCache
import com.github.sandroln.chosenboard.BoardScreenNavigation
import com.github.sandroln.cloudservice.MyUser
import com.github.sandroln.core.BaseViewModel
import com.github.sandroln.core.Communication
import com.github.sandroln.core.ProvideError
import com.github.sandroln.core.Reload

internal class BoardsViewModel(
    private val boardsNavigation: BoardsNavigation,
    private val myUser: MyUser,
    private val boardScreenNavigation: BoardScreenNavigation,
    dispatchersList: com.github.sandroln.core.DispatchersList,
    private val boardsRepository: BoardsRepository,
    private val communication: BoardsCommunication,
) : BaseViewModel(dispatchersList), BoardsViewModelActions {

    override fun observe(owner: LifecycleOwner, observer: Observer<BoardsUiState>) =
        communication.observe(owner, observer)

    override fun init(firstRun: Boolean) {
        myUser.checkDataInvalid()
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

    override fun openBoard(boardCache: BoardCache) {
        boardsRepository.save(boardCache)
        boardScreenNavigation.navigateToBoard()
    }

    override fun showProfile() = boardsNavigation.navigateFromBoards()
    override fun createBoard() = boardsNavigation.navigateToCreateBoard()
}

internal interface BoardsViewModelActions : com.github.sandroln.core.Init,
    Communication.Observe<BoardsUiState>, BoardClickListener,
    ReloadWithError {
    fun createBoard()
    fun showProfile()
}

internal interface ReloadWithError : Reload, ProvideError