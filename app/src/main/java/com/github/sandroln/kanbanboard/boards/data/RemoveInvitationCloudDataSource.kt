package com.github.sandroln.kanbanboard.boards.data

import com.github.sandroln.cloudservice.Service

interface RemoveInvitationCloudDataSource {

    fun removeInvitation(key: String)

    class Base(private val service: Service) : RemoveInvitationCloudDataSource {

        override fun removeInvitation(key: String) = service.remove("boards-invitations", key)
    }
}