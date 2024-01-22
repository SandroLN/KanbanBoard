package com.github.sandroln.kanbanboard.boards.data

import com.github.sandroln.cloudservice.Service

interface BoardCloudDataSource {

    interface Callback {

        fun provideBoard(boardId: String, data: Pair<String, BoardCloud>)

        object Empty : Callback {

            override fun provideBoard(boardId: String, data: Pair<String, BoardCloud>) = Unit
        }
    }

    fun init(callback: Callback)

    fun fetch(id: String, boardId: String)

    class Base(private val service: Service) : BoardCloudDataSource {

        private var callback: Callback = Callback.Empty

        override fun init(callback: Callback) {
            this.callback = callback
        }

        override fun fetch(id: String, boardId: String) = service.getByQueryAsyncOneTime(
            "boards",
            boardId,
            BoardCloud::class.java,
            object : Service.Callback<BoardCloud> {
                override fun provide(obj: List<Pair<String, BoardCloud>>) =
                    callback.provideBoard(boardId, Pair(id, obj[0].second))

                override fun error(message: String) = Unit//todo
            }
        )
    }
}