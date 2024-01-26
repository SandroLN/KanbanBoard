package com.github.sandroln.kanbanboard.boards.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.github.sandroln.chosenboard.BoardCache
import com.github.sandroln.cloudservice.MyUser
import com.github.sandroln.core.BaseViewModel
import com.github.sandroln.core.Communication
import com.github.sandroln.core.DispatchersList
import com.github.sandroln.core.Init
import com.github.sandroln.core.NavigationCommunication
import com.github.sandroln.core.ProvideError
import com.github.sandroln.core.Reload
import com.github.sandroln.kanbanboard.board.create.presentation.CreateBoardScreen
import com.github.sandroln.kanbanboard.board.main.presentation.BoardScreenNavigation
import com.github.sandroln.kanbanboard.boards.data.BoardsRepository
import com.github.sandroln.profile.presentation.ProfileScreen

class BoardsViewModel(
    private val myUser: MyUser,
    private val boardScreenNavigation: BoardScreenNavigation,
    private val navigation: NavigationCommunication.Update,
    dispatchersList: DispatchersList,
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

    override fun showProfile() = navigation.map(ProfileScreen)
    override fun createBoard() = navigation.map(CreateBoardScreen)
}

interface BoardsViewModelActions : Init, Communication.Observe<BoardsUiState>, BoardClickListener,
    ReloadWithError {
    fun createBoard()
    fun showProfile()
}

interface ReloadWithError : Reload, ProvideError