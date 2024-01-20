package com.github.sandroln.kanbanboard.boards.data

import com.github.sandroln.kanbanboard.core.ProvideDatabase

interface RemoveInvitationCloudDataSource {

    fun removeInvitation(key: String)

    class Base(private val provideDatabase: ProvideDatabase) : RemoveInvitationCloudDataSource {

        override fun removeInvitation(key: String) {
            provideDatabase.database()
                .child("boards-invitations")
                .child(key)
                .removeValue()
        }
    }
}