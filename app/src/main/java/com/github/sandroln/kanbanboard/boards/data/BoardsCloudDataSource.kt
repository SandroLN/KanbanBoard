package com.github.sandroln.kanbanboard.boards.data

import com.github.sandroln.kanbanboard.boards.presentation.ReloadWithError
import com.github.sandroln.kanbanboard.core.ProvideDatabase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface BoardsCloudDataSource : InitReloadCallback {

    suspend fun myBoards(): List<Board>

    suspend fun otherBoards(): List<Board>

    class Base(
        private val myBoardsNamesCache: MyBoardsNamesCache.Save,
        private val provideDatabase: ProvideDatabase
    ) : BoardsCloudDataSource {

        private val myBoardsCached = mutableListOf<Board>()
        private var loadedMyBoards = false
        private val otherBoardsIdsListCache = mutableListOf<String>()

        override fun init(reload: ReloadWithError) {
            val myUserId = Firebase.auth.currentUser!!.uid
            val query = provideDatabase.database()
                .child("boards-members")
                .orderByChild("memberId")
                .equalTo(myUserId)
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.children.mapNotNull {
                        it.getValue(OtherBoardCloud::class.java)?.boardId
                    }
                    otherBoardsIdsListCache.clear()
                    otherBoardsIdsListCache.addAll(data)
                    reload.reload()
                }

                override fun onCancelled(error: DatabaseError) = reload.error(error.message)
            })
        }

        override suspend fun myBoards(): List<Board> {
            if (!loadedMyBoards) {
                val myUserId = Firebase.auth.currentUser!!.uid
                val query = provideDatabase.database()
                    .child("boards")
                    .orderByChild("owner")
                    .equalTo(myUserId)
                val sourceList = HandleBoards(query).list()
                val list = sourceList.map { (id, boardCloud) -> Board.MyBoard(id, boardCloud.name) }
                myBoardsCached.addAll(list)
                val myBoardsNameList = sourceList.map { (_, boardCloud) -> boardCloud.name }
                myBoardsNamesCache.save(myBoardsNameList)
                loadedMyBoards = true
            }
            return myBoardsCached
        }

        override suspend fun otherBoards(): List<Board> {
            val list = mutableListOf<Board>()
            otherBoardsIdsListCache.forEach { boardId ->
                val query = provideDatabase.database()
                    .child("boards")
                    .orderByKey()
                    .equalTo(boardId)

                val boards =
                    HandleBoards(query).list().map { (id, boardCloud) ->
                        Board.OtherBoard(
                            id,
                            boardCloud.name,
                            boardCloud.owner
                        )
                    }
                list.addAll(boards)
            }
            return list
        }
    }
}

private class HandleBoards(private val query: Query) {

    suspend fun list(): List<Pair<String, BoardCloud>> = suspendCoroutine { cont ->
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.children.mapNotNull {
                    Pair(
                        it.key!!,
                        it.getValue(BoardCloud::class.java)!!
                    )
                }
                cont.resume(data)
            }

            override fun onCancelled(error: DatabaseError) =
                cont.resumeWithException(IllegalStateException(error.message))
        })
    }
}

data class BoardCloud(
    val owner: String = "",
    val name: String = ""
)

private data class OtherBoardCloud(
    val memberId: String = "",
    val boardId: String = ""
)