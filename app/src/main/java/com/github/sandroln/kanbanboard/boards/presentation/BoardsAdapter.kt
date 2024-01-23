package com.github.sandroln.kanbanboard.boards.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.github.sandroln.kanbanboard.R

class BoardsAdapter(
    private val clickListener: BoardClickListener,
    private val typeList: List<BoardType> =
        listOf(BoardType.Progress, BoardType.Title, BoardType.Name, BoardType.Hint, BoardType.Error)
) : RecyclerView.Adapter<BoardViewHolder>(), com.github.sandroln.core.Mapper.Unit<List<BoardUi>> {

    private val boardList = mutableListOf<BoardUi>()

    override fun getItemViewType(position: Int) = boardList[position].orderId()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        typeList.find { it.intValue() == viewType }?.viewHolder(parent, clickListener)
            ?: throw IllegalStateException("unknown viewType $viewType")

    override fun getItemCount() = boardList.size

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) =
        holder.bind(boardList[position])

    override fun map(source: List<BoardUi>) {
        val diff = DiffUtilCallback(boardList, source)
        val result = DiffUtil.calculateDiff(diff)
        boardList.clear()
        boardList.addAll(source)
        result.dispatchUpdatesTo(this)
    }
}

interface BoardType {

    fun intValue(): Int

    fun viewHolder(parent: ViewGroup, clickListener: BoardClickListener): BoardViewHolder

    object Progress : BoardType {

        override fun intValue() = 0

        override fun viewHolder(
            parent: ViewGroup,
            clickListener: BoardClickListener
        ) = BoardViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.board_progress, parent, false)
        )
    }

    object Title : BoardType {
        override fun intValue() = 1

        override fun viewHolder(
            parent: ViewGroup,
            clickListener: BoardClickListener
        ) = BoardTitleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.board_title, parent, false)
        )
    }

    object Name : BoardType {
        override fun intValue() = 2
        override fun viewHolder(
            parent: ViewGroup,
            clickListener: BoardClickListener
        ) = BoardNameViewHolder(
            clickListener,
            LayoutInflater.from(parent.context)
                .inflate(R.layout.board_name, parent, false)
        )
    }

    object Hint : BoardType {
        override fun intValue() = 3

        override fun viewHolder(
            parent: ViewGroup,
            clickListener: BoardClickListener
        ) = BoardTitleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.board_hint, parent, false)
        )
    }

    object Error : BoardType {
        override fun intValue() = 4
        override fun viewHolder(
            parent: ViewGroup,
            clickListener: BoardClickListener
        ) = BoardErrorViewHolder(
            clickListener,
            LayoutInflater.from(parent.context)
                .inflate(R.layout.board_error, parent, false)
        )
    }
}

open class BoardViewHolder(view: View) : ViewHolder(view) {

    open fun bind(item: BoardUi) = Unit
}

class BoardTitleViewHolder(view: View) : BoardViewHolder(view) {
    private val textView = itemView.findViewById<TextView>(R.id.boardTitleTextView)
    override fun bind(item: BoardUi) = item.map(textView)
}

class BoardNameViewHolder(
    private val openBoard: OpenBoard,
    view: View
) : BoardViewHolder(view) {

    private val button = itemView.findViewById<Button>(R.id.boardNameButton)
    override fun bind(item: BoardUi) {
        item.map(button)
        button.setOnClickListener {
            item.openBoard(openBoard)
        }
    }
}

class BoardErrorViewHolder(
    private val retry: com.github.sandroln.core.Retry,
    view: View
) : BoardViewHolder(view) {

    private val errorTextView = itemView.findViewById<TextView>(R.id.errorTextView)
    private val retryButton = itemView.findViewById<Button>(R.id.retryButton)
    override fun bind(item: BoardUi) {
        item.map(errorTextView)
        retryButton.setOnClickListener {
            retry.retry()
        }
    }
}

interface BoardClickListener : com.github.sandroln.core.Retry, OpenBoard

interface OpenBoard {

    fun openBoard(boardInfo: BoardInfo)
}

private class DiffUtilCallback(
    private val oldList: List<BoardUi>,
    private val newList: List<BoardUi>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id() == newList[newItemPosition].id()


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}