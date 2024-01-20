package com.github.sandroln.kanbanboard.boards.data

import com.github.sandroln.kanbanboard.core.ProvideDatabase

interface AcceptInvitationCloudDataSource {

    fun acceptInvitation(userId: String, boardId: String)

    class Base(
        private val provideDatabase: ProvideDatabase
    ) : AcceptInvitationCloudDataSource {

        override fun acceptInvitation(userId: String, boardId: String) {
            provideDatabase.database()
                .child("boards-members")
                .push()
                .setValue(OtherBoardCloud(userId, boardId))
        }
    }
}