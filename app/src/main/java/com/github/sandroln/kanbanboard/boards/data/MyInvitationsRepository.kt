package com.github.sandroln.kanbanboard.boards.data

import com.github.sandroln.kanbanboard.boards.presentation.BoardInvitation
import com.github.sandroln.kanbanboard.boards.presentation.InvitationActions
import com.github.sandroln.kanbanboard.login.data.MyUser

interface MyInvitationsRepository : InvitationActions {

    interface Callback {

        fun provideInvitations(list: List<BoardInvitation>)

        object Empty : Callback {
            override fun provideInvitations(list: List<BoardInvitation>) = Unit
        }
    }

    fun init(callback: Callback)

    class Base(
        private val invitationsCloudDataSource: Invitations.CloudDataSource,
        private val boardCloudDataSource: BoardCloudDataSource,
        private val acceptInvitationCloudDataSource: AcceptInvitationCloudDataSource,
        private val removeInvitationCloudDataSource: RemoveInvitationCloudDataSource,
        private val myUser: MyUser
    ) : MyInvitationsRepository, Invitations.Callback, BoardCloudDataSource.Callback {

        private var callback: Callback = Callback.Empty

        private var boardsCache = mutableMapOf<String, BoardInvitation>()
        private var boardsSet = mutableSetOf<BoardInvitation>()

        override fun provideInvitations(list: List<Pair<String, OtherBoardCloud>>) =
            synchronized(lock) {
                boardsSet.clear()
                list.map { Pair(it.first, it.second.boardId) }.forEach { (id, boardId) ->
                    boardsCache[boardId]?.let { boardsSet.add(it) }
                        ?: boardCloudDataSource.fetch(id, boardId)
                }
                callback.provideInvitations(boardsSet.toList())
            }

        override fun init(callback: Callback) {
            this.callback = callback
            invitationsCloudDataSource.init(this)
            boardCloudDataSource.init(this)
            invitationsCloudDataSource.handle(myUser.id())
        }

        override fun accept(id: String, boardId: String) {
            acceptInvitationCloudDataSource.acceptInvitation(myUser.id(), boardId)
            decline(id)
        }

        override fun decline(id: String) = removeInvitationCloudDataSource.removeInvitation(id)

        override fun provideBoard(boardId: String, data: Pair<String, BoardCloud>) =
            synchronized(lock) {
                val (id, board) = data
                val invitation = BoardInvitation(id, boardId, board.name)
                boardsCache[boardId] = invitation
                boardsSet.add(invitation)
                callback.provideInvitations(boardsSet.toList())
            }
    }

    companion object {
        private val lock = Object()
    }
}