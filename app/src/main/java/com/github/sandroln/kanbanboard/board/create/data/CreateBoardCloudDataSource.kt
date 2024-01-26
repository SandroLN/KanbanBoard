package com.github.sandroln.kanbanboard.board.create.data

import com.github.sandroln.chosenboard.BoardCache
import com.github.sandroln.cloudservice.MyUser
import com.github.sandroln.cloudservice.Service
import com.github.sandroln.kanbanboard.boards.data.BoardCloud

interface CreateBoardCloudDataSource {

    suspend fun createBoard(name: String): BoardCache

    class Base(
        private val myUser: MyUser,
        private val service: Service
    ) : CreateBoardCloudDataSource {

        override suspend fun createBoard(name: String): BoardCache {
            val myId = myUser.id()
            val id = service.postFirstLevel("boards", BoardCloud(myId, name))
            return BoardCache(id, name, true)
        }
    }
}