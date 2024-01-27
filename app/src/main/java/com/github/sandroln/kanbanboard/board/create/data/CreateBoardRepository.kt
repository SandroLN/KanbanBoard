package com.github.sandroln.kanbanboard.board.create.data

import com.github.sandroln.chosenboard.ChosenBoardCache
import com.github.sandroln.chosenboard.MyBoardsNamesCache

interface CreateBoardRepository {

    fun contains(name: String): Boolean

    suspend fun create(name: String): CreateBoardResult

    class Base(
        private val createBoardCloudDataSource: CreateBoardCloudDataSource,
        private val chosenBoardCache: ChosenBoardCache.Save,
        namesCache: MyBoardsNamesCache.Read
    ) : CreateBoardRepository {

        private val namesCachedList = namesCache.read().map { it.lowercase() }

        override fun contains(name: String) = namesCachedList.contains(name.lowercase())

        override suspend fun create(name: String) = try {
            val board = createBoardCloudDataSource.createBoard(name)
            chosenBoardCache.save(board)
            CreateBoardResult.Success
        } catch (e: Exception) {
            CreateBoardResult.Failed(e.message ?: "error")
        }
    }
}