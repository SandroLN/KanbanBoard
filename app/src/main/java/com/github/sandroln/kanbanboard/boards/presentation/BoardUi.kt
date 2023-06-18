package com.github.sandroln.kanbanboard.boards.presentation

import android.widget.TextView
import com.github.sandroln.kanbanboard.R
import com.github.sandroln.kanbanboard.board.data.BoardCloudDataSource
import com.github.sandroln.kanbanboard.board.presentation.BoardCommunication
import com.github.sandroln.kanbanboard.board.presentation.BoardUiState
import com.github.sandroln.kanbanboard.core.Mapper

interface BoardUi {
    fun id(): String
    fun openBoard(open: OpenBoard) = Unit
    fun orderId(): Int
    fun map(textView: TextView)

    object Progress : BoardUi {
        override fun map(textView: TextView) = Unit
        override fun orderId() = 0
        override fun id() = "BoardUiProgress"
    }

    object MyBoardTitle : BoardUi {
        override fun map(textView: TextView) = textView.setText(R.string.my_boards_title)
        override fun orderId() = 1
        override fun id() = "BoardUiMyBoardTitle"
    }

    data class MyBoard(private val key: String, private val name: String) : BoardUi {
        override fun map(textView: TextView) {
            textView.text = name
        }

        override fun openBoard(open: OpenBoard) = open.openBoard(BoardInfo(key, name, true))
        override fun orderId() = 2
        override fun id() = key
    }

    object NoBoardsOfMyOwnHint : BoardUi {
        override fun map(textView: TextView) = textView.setText(R.string.no_boards_of_my_own)
        override fun orderId() = 3
        override fun id() = "BoardUiNoBoardsOfMyOwnHint"
    }

    object OtherBoardsTitle : BoardUi {
        override fun map(textView: TextView) = textView.setText(R.string.other_boards_title)
        override fun orderId() = 4
        override fun id() = "BoardUiOtherBoardsTitle"
    }

    data class OtherBoard(
        private val key: String,
        private val name: String,
        private val owner: String
    ) : BoardUi {
        override fun map(textView: TextView) {
            textView.text = name
        }

        override fun openBoard(open: OpenBoard) = open.openBoard(BoardInfo(key, name, false, owner))

        override fun orderId() = 5
        override fun id() = key
    }

    object HowToBeAddedToBoardHint : BoardUi {
        override fun map(textView: TextView) = textView.setText(R.string.how_to_be_added_to_board)
        override fun orderId() = 6
        override fun id() = "BoardUiHowToBeAddedToBoardHint"
    }

    data class Error(private val message: String) : BoardUi {
        override fun map(textView: TextView) {
            textView.text = message
        }

        override fun orderId() = 7
        override fun id() = "BoardUiError$message"
    }
}

data class BoardInfo(
    private val id: String,
    private val name: String,
    private val isMyBoard: Boolean,
    private val ownerId: String = ""
) : Mapper.Unit<BoardCommunication> {

    override fun map(source: BoardCommunication) =
        source.map(BoardUiState.Initial(name, isMyBoard))

    fun init(boardCloudDataSource: BoardCloudDataSource) =
        boardCloudDataSource.init(id, isMyBoard, ownerId)
}