package com.github.sandroln.kanbanboard.boards.data

import com.github.sandroln.core.Save
import com.github.sandroln.kanbanboard.boards.presentation.BoardInfo
import com.github.sandroln.kanbanboard.boards.presentation.ReloadWithError

interface BoardsRepository : InitReloadCallback, Save<BoardInfo> {

    suspend fun data(): List<Board>

    class Base(
        private val saveBoard: ChosenBoardCache.Save,
        private val cloudDataSource: BoardsCloudDataSource
    ) : BoardsRepository {

        override suspend fun data() = try {
            val list = mutableListOf<Board>()
            list.add(Board.MyOwnBoardsTitle)
            val myOwnBoards = cloudDataSource.myBoards()
            if (myOwnBoards.isEmpty())
                list.add(Board.NoBoardsOfMyOwnHint)
            else
                list.addAll(myOwnBoards)
            list.add(Board.OtherBoardsTitle)
            val otherBoards = cloudDataSource.otherBoards()
            if (otherBoards.isEmpty())
                list.add(Board.HowToBeAddedToBoardHint)
            else
                list.addAll(otherBoards)
            list
        } catch (e: Exception) {
            listOf(Board.Error(e.message ?: "error"))
        }

        override fun init(reload: ReloadWithError) = cloudDataSource.init(reload)

        override fun save(data: BoardInfo) = saveBoard.save(data)
    }
}

interface InitReloadCallback {
    fun init(reload: ReloadWithError)
}