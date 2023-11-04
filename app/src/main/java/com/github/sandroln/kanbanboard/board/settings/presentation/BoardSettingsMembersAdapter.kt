package com.github.sandroln.kanbanboard.board.settings.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.sandroln.kanbanboard.R
import com.github.sandroln.kanbanboard.board.main.data.BoardUser
import com.github.sandroln.kanbanboard.core.Mapper
import com.github.sandroln.kanbanboard.ticket.common.presentation.BoardUserDiff
import com.github.sandroln.kanbanboard.ticket.common.presentation.MemberViewHolder
import com.github.sandroln.kanbanboard.ticket.create.presentation.AssignUser

class BoardSettingsMembersAdapter(//todo common adapter MembersAdapter
    private val assignUser: AssignUser
) : RecyclerView.Adapter<MemberViewHolder>(), Mapper.Unit<List<BoardUser>> {

    private val list = mutableListOf<BoardUser>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MemberViewHolder(
        assignUser,
        LayoutInflater.from(parent.context).inflate(R.layout.board_name, parent, false)
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) =
        holder.bind(list[position], true)

    override fun map(source: List<BoardUser>) {
        val diff = BoardUserDiff(list, source)
        val result = DiffUtil.calculateDiff(diff)
        list.clear()
        list.addAll(source)
        result.dispatchUpdatesTo(this)
    }
}