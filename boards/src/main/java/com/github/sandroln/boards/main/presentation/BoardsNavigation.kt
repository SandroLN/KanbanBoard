package com.github.sandroln.boards.main.presentation

interface BoardsNavigation : NavigateFromBoards, NavigateToCreateBoard

interface NavigateFromBoards {

    fun navigateFromBoards()
}

interface NavigateToCreateBoard {

    fun navigateToCreateBoard()
}