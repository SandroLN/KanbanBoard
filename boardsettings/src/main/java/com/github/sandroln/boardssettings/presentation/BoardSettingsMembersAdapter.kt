package com.github.sandroln.boardssettings.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.sandroln.boardssettings.R
import com.github.sandroln.core.Mapper
import com.github.sandroln.openedboard.AssignUser
import com.github.sandroln.openedboard.BoardUser
import com.github.sandroln.openedboard.BoardUserDiff
import com.github.sandroln.openedboard.MemberViewHolder

internal class BoardSettingsMembersAdapter(//todo common adapter MembersAdapter
    private val assignUser: AssignUser
) : RecyclerView.Adapter<MemberViewHolder>(), Mapper.Unit<List<BoardUser>> {

    private val list = mutableListOf<BoardUser>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MemberViewHolder(
        assignUser, //todo duplicated_board_name
        LayoutInflater.from(parent.context).inflate(R.layout.duplicated_board_name, parent, false)
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