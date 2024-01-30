package com.github.sandroln.boards.myinvitations.data

import com.github.sandroln.chosenboard.OtherBoardCloud
import com.github.sandroln.cloudservice.Service

internal interface AcceptInvitationCloudDataSource {

    fun acceptInvitation(userId: String, boardId: String)

    class Base(private val service: Service) : AcceptInvitationCloudDataSource {

        override fun acceptInvitation(userId: String, boardId: String) =
            service.postFirstLevelAsync("boards-members", OtherBoardCloud(userId, boardId))
    }
}