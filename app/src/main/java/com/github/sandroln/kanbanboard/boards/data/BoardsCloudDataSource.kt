package com.github.sandroln.kanbanboard.boards.data

import com.github.sandroln.cloudservice.MyUser
import com.github.sandroln.cloudservice.Service
import com.github.sandroln.kanbanboard.boards.presentation.ReloadWithError

interface BoardsCloudDataSource : InitReloadCallback {

    suspend fun myBoards(): List<Board>

    suspend fun otherBoards(): List<Board>

    class Base(
        private val myUser: MyUser,
        private val myBoardsNamesCache: MyBoardsNamesCache.Save,
        private val service: Service
    ) : BoardsCloudDataSource {

        private val myBoardsCached = mutableListOf<Board>()
        private var loadedMyBoards = false
        private val otherBoardsIdsListCache = mutableListOf<String>()

        override fun init(reload: ReloadWithError) {
            val myUserId = myUser.id()
            service.getByQueryAsync(
                "boards-members",
                "memberId",
                myUserId,
                OtherBoardCloud::class.java,
                object : Service.Callback<OtherBoardCloud> {
                    override fun provide(obj: List<Pair<String, OtherBoardCloud>>) {
                        val data = obj.map { it.second.boardId }
                        otherBoardsIdsListCache.clear()
                        otherBoardsIdsListCache.addAll(data)
                        reload.reload()
                    }

                    override fun error(message: String) = reload.error(message)
                }
            )
        }

        override suspend fun myBoards(): List<Board> {
            if (!loadedMyBoards) {
                val myUserId = myUser.id()
                val sourceList = service.getByQuery(
                    "boards",
                    "owner",
                    myUserId,
                    BoardCloud::class.java
                )
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
                val query = service.getByQuery("boards", boardId, BoardCloud::class.java)
                val boards = query.map { (id, boardCloud) ->
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

data class BoardCloud(
    val owner: String = "",
    val name: String = ""
)

data class OtherBoardCloud(
    val memberId: String = "",
    val boardId: String = ""
)