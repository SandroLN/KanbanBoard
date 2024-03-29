package com.github.sandroln.openedboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class MembersAdapter(
    private val assignUser: AssignUser,
) : RecyclerView.Adapter<MemberViewHolder>(), UpdateQuery {

    private var query: String = ""
    private val allMembers = mutableListOf<BoardUser>()
    private val visibleList = mutableListOf<BoardUser>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MemberViewHolder(
        assignUser,//todo duplicated_board_name
        LayoutInflater.from(parent.context).inflate(R.layout.duplicated_board_name, parent, false)
    )

    override fun getItemCount() = visibleList.size

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) =
        holder.bind(visibleList[position])

    fun updateMembers(users: List<BoardUser>) {
        allMembers.clear()
        allMembers.addAll(users)
        updateQuery(query)
    }

    override fun updateQuery(newQuery: String) {
        query = newQuery
        val newList = if (query.isEmpty())
            allMembers
        else
            allMembers.filter {
                it.name().contains(query, true)
            }
        val diff = BoardUserDiff(visibleList, newList)
        val result = DiffUtil.calculateDiff(diff)
        visibleList.clear()
        visibleList.addAll(newList)
        result.dispatchUpdatesTo(this)
    }
}

class MemberViewHolder(
    private val assignUser: AssignUser,
    view: View
) : RecyclerView.ViewHolder(view) {

    private val memberNameButton = itemView.findViewById<Button>(R.id.boardNameButton)

    fun bind(user: BoardUser, showEmail: Boolean = false) = with(memberNameButton) {
        text = if (showEmail) user.nameWithEmail() else user.name()
        setOnClickListener {
            assignUser.assign(user)
        }
    }
}

class BoardUserDiff(
    private val old: List<BoardUser>,
    private val new: List<BoardUser>
) : DiffUtil.Callback() {

    override fun getOldListSize() = old.size
    override fun getNewListSize() = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        old[oldItemPosition].id() == new[newItemPosition].id()

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        old[oldItemPosition] == new[newItemPosition]
}

interface UpdateQuery {
    fun updateQuery(newQuery: String)
}