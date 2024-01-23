package com.github.sandroln.kanbanboard.board.create.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.github.sandroln.core.NavigationCommunication
import com.github.sandroln.kanbanboard.R
import com.github.sandroln.kanbanboard.board.create.data.CreateBoardRepository
import com.github.sandroln.kanbanboard.board.main.presentation.BoardScreenNavigation
import com.github.sandroln.kanbanboard.boards.presentation.BoardsScreen

class CreateBoardViewModel(
    private val boardScreenNavigation: BoardScreenNavigation,
    private val manageResource: com.github.sandroln.core.ManageResource,
    private val repository: CreateBoardRepository,
    private val communication: CreateBoardCommunication,
    private val navigation: NavigationCommunication.Update,
    dispatchersList: com.github.sandroln.core.DispatchersList
) : com.github.sandroln.core.BaseViewModel(dispatchersList), CreateBoardActions {

    override fun disableCreate() = communication.map(CreateBoardUiState.CanNotCreateBoard)

    override fun observe(owner: LifecycleOwner, observer: Observer<CreateBoardUiState>) =
        communication.observe(owner, observer)

    override fun checkBoard(name: String) = communication.map(
        if (repository.contains(name))
            CreateBoardUiState.BoardAlreadyExists(manageResource.string(R.string.board_already_exists))
        else
            CreateBoardUiState.CanCreateBoard
    )

    override fun createBoard(name: String) {
        communication.map(CreateBoardUiState.Progress)
        handle({ repository.create(name) }) { it.map(boardScreenNavigation, communication) }
    }

    override fun goBack() = navigation.map(BoardsScreen)
}

interface CreateBoardActions : CreateBoardUiActions, com.github.sandroln.core.GoBack,
    com.github.sandroln.core.Communication.Observe<CreateBoardUiState> {
    fun createBoard(name: String)
}

interface CreateBoardUiActions {
    fun checkBoard(name: String)
    fun disableCreate()
}