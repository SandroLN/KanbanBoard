package com.github.sandroln.kanbanboard.boards.data

import com.github.sandroln.cloudservice.Service

interface AcceptInvitationCloudDataSource {

    fun acceptInvitation(userId: String, boardId: String)

    class Base(private val service: Service) : AcceptInvitationCloudDataSource {

        override fun acceptInvitation(userId: String, boardId: String) =
            service.postFirstLevelAsync("boards-members", OtherBoardCloud(userId, boardId))
    }
}