package com.github.sandroln.boards.myinvitations.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.sandroln.boards.R

internal class InvitationsAdapter(
    private val actions: InvitationActions
) : RecyclerView.Adapter<InvitationViewHolder>(), com.github.sandroln.core.Mapper.Unit<List<BoardInvitation>> {

    private val list = mutableListOf<BoardInvitation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = InvitationViewHolder(
        actions,
        LayoutInflater.from(parent.context).inflate(R.layout.invitation, parent, false)
    )

    override fun onBindViewHolder(holder: InvitationViewHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount() = list.size

    override fun map(source: List<BoardInvitation>) {
        val diff = Diff(list, source)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(source)
        result.dispatchUpdatesTo(this)
    }
}

internal class InvitationViewHolder(
    private val actions: InvitationActions,
    view: View
) : RecyclerView.ViewHolder(view) {

    private val name: TextView = itemView.findViewById(R.id.boardNameTextView)
    private val acceptButton: Button = itemView.findViewById(R.id.acceptButton)
    private val declineButton: Button = itemView.findViewById(R.id.declineButton)

    fun bind(invitation: BoardInvitation) {
        invitation.show(name)
        acceptButton.setOnClickListener {
            invitation.accept(actions)
        }
        declineButton.setOnClickListener {
            invitation.decline(actions)
        }
    }
}

internal data class BoardInvitation(
    private val id: String,
    private val boardId: String,
    private val boardName: String
) {

    fun show(textView: TextView) = textView.setText(boardName)

    fun accept(actions: InvitationActions) = actions.accept(id, boardId)

    fun decline(actions: InvitationActions) = actions.decline(id)

    fun same(other: BoardInvitation): Boolean = boardId == other.boardId
}

internal interface InvitationActions {

    fun accept(id: String, boardId: String)

    fun decline(id: String)
}

private class Diff(
    private val oldList: List<BoardInvitation>,
    private val newList: List<BoardInvitation>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].same(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}