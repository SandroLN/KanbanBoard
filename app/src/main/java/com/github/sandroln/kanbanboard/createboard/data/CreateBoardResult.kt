package com.github.sandroln.kanbanboard.createboard.data

import com.github.sandroln.kanbanboard.createboard.presentation.BoardScreen
import com.github.sandroln.kanbanboard.createboard.presentation.CreateBoardCommunication
import com.github.sandroln.kanbanboard.createboard.presentation.CreateBoardUiState
import com.github.sandroln.kanbanboard.main.NavigationCommunication

interface CreateBoardResult {

    fun map(navigation: NavigationCommunication.Update, communication: CreateBoardCommunication)

    object Success : CreateBoardResult {
        override fun map(
            navigation: NavigationCommunication.Update,
            communication: CreateBoardCommunication
        ) = navigation.map(BoardScreen)
    }

    data class Failed(private val errorMessage: String) : CreateBoardResult {
        override fun map(
            navigation: NavigationCommunication.Update,
            communication: CreateBoardCommunication
        ) = communication.map(CreateBoardUiState.Error(errorMessage))
    }
}