package com.github.sandroln.createboard.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.github.sandroln.chosenboard.BoardScreenNavigation
import com.github.sandroln.common.NavigateToBoards
import com.github.sandroln.core.Communication
import com.github.sandroln.core.GoBack
import com.github.sandroln.createboard.R
import com.github.sandroln.createboard.data.CreateBoardRepository

internal class CreateBoardViewModel(
    private val boardScreenNavigation: BoardScreenNavigation,
    private val manageResource: com.github.sandroln.core.ManageResource,
    private val repository: CreateBoardRepository,
    private val communication: CreateBoardCommunication,
    private val navigation: NavigateToBoards,
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

    override fun goBack() = navigation.navigateToBoards()
}

internal interface CreateBoardActions : CreateBoardUiActions, GoBack,
    Communication.Observe<CreateBoardUiState> {
    fun createBoard(name: String)
}

internal interface CreateBoardUiActions {
    fun checkBoard(name: String)
    fun disableCreate()
}