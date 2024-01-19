package com.github.sandroln.kanbanboard.boards.presentation

import android.widget.TextView
import com.github.sandroln.kanbanboard.R
import com.github.sandroln.kanbanboard.board.main.data.BoardCloudDataSource
import com.github.sandroln.kanbanboard.board.main.data.BoardUser
import com.github.sandroln.kanbanboard.board.main.presentation.BoardToolbarCommunication
import com.github.sandroln.kanbanboard.board.main.presentation.BoardToolbarUi
import com.github.sandroln.kanbanboard.board.settings.data.Invitations
import com.github.sandroln.kanbanboard.boards.data.OtherBoardCloud
import com.github.sandroln.kanbanboard.ticket.create.data.CreateTicketOnBoard

interface BoardUi {
    fun id(): String
    fun openBoard(open: OpenBoard) = Unit
    fun orderId(): Int
    fun map(textView: TextView)

    object Progress : BoardUi {
        override fun map(textView: TextView) = Unit
        override fun orderId() = BoardType.Progress.intValue()
        override fun id() = "BoardUiProgress"
    }

    object MyBoardTitle : BoardUi {
        override fun map(textView: TextView) = textView.setText(R.string.my_boards_title)
        override fun orderId() = BoardType.Title.intValue()
        override fun id() = "BoardUiMyBoardTitle"
    }

    data class MyBoard(private val key: String, private val name: String) : BoardUi {
        override fun orderId() = BoardType.Name.intValue()
        override fun map(textView: TextView) {
            textView.text = name
        }

        override fun openBoard(open: OpenBoard) = open.openBoard(BoardInfo(key, name, true))
        override fun id() = key
    }

    object NoBoardsOfMyOwnHint : BoardUi {
        override fun orderId() = BoardType.Hint.intValue()
        override fun map(textView: TextView) = textView.setText(R.string.no_boards_of_my_own)
        override fun id() = "BoardUiNoBoardsOfMyOwnHint"
    }

    object OtherBoardsTitle : BoardUi {
        override fun orderId() = BoardType.Title.intValue()
        override fun map(textView: TextView) = textView.setText(R.string.other_boards_title)
        override fun id() = "BoardUiOtherBoardsTitle"
    }

    data class OtherBoard(
        private val key: String,
        private val name: String,
        private val owner: String
    ) : BoardUi {
        override fun orderId() = BoardType.Name.intValue()

        override fun map(textView: TextView) {
            textView.text = name
        }

        override fun openBoard(open: OpenBoard) = open.openBoard(BoardInfo(key, name, false, owner))

        override fun id() = key
    }

    object HowToBeAddedToBoardHint : BoardUi {
        override fun orderId() = BoardType.Hint.intValue()

        override fun map(textView: TextView) = textView.setText(R.string.how_to_be_added_to_board)
        override fun id() = "BoardUiHowToBeAddedToBoardHint"
    }

    data class Error(private val message: String) : BoardUi {
        override fun orderId() = BoardType.Error.intValue()
        override fun map(textView: TextView) {
            textView.text = message
        }

        override fun id() = "BoardUiError$message"
    }
}

data class BoardInfo(
    private val id: String,
    private val name: String,
    private val isMyBoard: Boolean,
    private val ownerId: String = ""
) {
    fun invite(cloudDataSource: Invitations.CloudDataSource.Handle) = cloudDataSource.handle(id)

    fun invite(user: BoardUser) = OtherBoardCloud(user.id(), id)

    fun createTicket(createTicket: CreateTicketOnBoard) = createTicket.createTicket(id)

    fun show(communication: BoardToolbarCommunication) =
        communication.map(BoardToolbarUi(name, isMyBoard))

    fun init(boardCloudDataSource: BoardCloudDataSource) =
        boardCloudDataSource.init(id, isMyBoard, ownerId)
}