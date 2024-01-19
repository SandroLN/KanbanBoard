package com.github.sandroln.kanbanboard.board.settings.data

import com.github.sandroln.kanbanboard.board.main.data.BoardUser
import com.github.sandroln.kanbanboard.board.main.data.MemberName
import com.github.sandroln.kanbanboard.boards.data.ChosenBoardCache
import com.github.sandroln.kanbanboard.login.data.UserProfileCloud

interface BoardInvitationRepository {

    fun init(callback: Callback)

    interface Callback {

        fun provideInvitations(users: List<BoardUser>)

        object Empty : Callback {

            override fun provideInvitations(users: List<BoardUser>) = Unit
        }
    }

    class Base( //todo unit test
        private val invitationsCloudDataSource: Invitations.CloudDataSource.Mutable,
        private val memberCloudDataSource: MemberName.CloudDataSource,
        private val chosenBoardCache: ChosenBoardCache.Read
    ) : BoardInvitationRepository, MemberName.Callback, Invitations.Callback {

        private var callback: Callback = Callback.Empty

        override fun init(callback: Callback) {
            this.callback = callback
            memberCloudDataSource.init(this)
            invitationsCloudDataSource.init(this)
            chosenBoardCache.read().invite(invitationsCloudDataSource)
        }

        private val usersCache = mutableMapOf<String, BoardUser>()
        private val usersSet = mutableSetOf<BoardUser>()

        override fun provideInvitations(invitedMemberIds: List<String>) = synchronized(lock) {
            usersSet.clear()
            invitedMemberIds.forEach { id ->
                usersCache[id]?.let { usersSet.add(it) } ?: memberCloudDataSource.handle(id)
            }
            callback.provideInvitations(usersSet.toList())
        }

        override fun provideMember(user: UserProfileCloud, userId: String) = synchronized(lock) {
            val boardUser = BoardUser.Base(userId, user.name, user.mail)
            usersCache[userId] = boardUser
            usersSet.add(boardUser)
            callback.provideInvitations(usersSet.toList())
        }

        companion object {
            private val lock = Object()
        }
    }
}