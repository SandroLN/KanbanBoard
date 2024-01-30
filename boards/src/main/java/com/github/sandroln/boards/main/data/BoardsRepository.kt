package com.github.sandroln.boards.main.data

import com.github.sandroln.boards.main.presentation.ReloadWithError
import com.github.sandroln.chosenboard.BoardCache
import com.github.sandroln.chosenboard.ChosenBoardCache
import com.github.sandroln.core.Save

internal interface BoardsRepository : InitReloadCallback, Save<BoardCache> {

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

        override fun save(data: BoardCache) = saveBoard.save(data)
    }
}

internal interface InitReloadCallback {
    fun init(reload: ReloadWithError)
}