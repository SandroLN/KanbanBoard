package com.github.sandroln.boards.myinvitations.data

import com.github.sandroln.cloudservice.Service

internal interface RemoveInvitationCloudDataSource {

    fun removeInvitation(key: String)

    class Base(private val service: Service) : RemoveInvitationCloudDataSource {

        override fun removeInvitation(key: String) = service.remove("boards-invitations", key)
    }
}