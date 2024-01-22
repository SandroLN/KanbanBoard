package com.github.sandroln.kanbanboard.board.create.data

import com.github.sandroln.cloudservice.MyUser
import com.github.sandroln.cloudservice.Service
import com.github.sandroln.kanbanboard.boards.data.BoardCloud
import com.github.sandroln.kanbanboard.boards.presentation.BoardInfo

interface CreateBoardCloudDataSource {

    suspend fun createBoard(name: String): BoardInfo

    class Base(
        private val myUser: MyUser,
        private val service: Service
    ) : CreateBoardCloudDataSource {

        override suspend fun createBoard(name: String): BoardInfo {
            val myId = myUser.id()
            val id = service.postFirstLevel("boards", BoardCloud(myId, name))
            return BoardInfo(id, name, true)
        }
    }
}