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
import com.github.sandroln.kanbanboard.core.Mapper
import com.github.sandroln.kanbanboard.core.Retry

class BoardsAdapter(
    private val clickListener: BoardClickListener
) : RecyclerView.Adapter<BoardViewHolder>(), Mapper.Unit<List<BoardUi>> {

    private val boardList = mutableListOf<BoardUi>()

    override fun getItemViewType(position: Int) = boardList[position].orderId()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        0 -> BoardViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.board_progress, parent, false)
        )

        1, 4 -> BoardTitleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.board_title, parent, false)
        )

        2, 5 -> BoardNameViewHolder(
            clickListener,
            LayoutInflater.from(parent.context)
                .inflate(R.layout.board_name, parent, false)
        )

        3, 6 -> BoardTitleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.board_hint, parent, false)
        )

        7 -> BoardErrorViewHolder(
            clickListener,
            LayoutInflater.from(parent.context)
                .inflate(R.layout.board_error, parent, false)
        )

        else -> throw IllegalStateException("unknown viewType $viewType")
    }

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

open class BoardViewHolder(view: View) : ViewHolder(view) {

    open fun bind(item: BoardUi) = Unit
}

private class BoardTitleViewHolder(view: View) : BoardViewHolder(view) {
    private val textView = itemView.findViewById<TextView>(R.id.boardTitleTextView)
    override fun bind(item: BoardUi) = item.map(textView)
}

private class BoardNameViewHolder(
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

private class BoardErrorViewHolder(
    private val retry: Retry,
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

interface BoardClickListener : Retry, OpenBoard

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