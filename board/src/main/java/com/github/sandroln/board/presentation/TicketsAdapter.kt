package com.github.sandroln.board.presentation

import android.content.ClipData
import android.content.ClipDescription
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.sandroln.board.R
import com.github.sandroln.core.Mapper
import com.github.sandroln.core.Serialization
import com.github.sandroln.openedboard.Column
import com.github.sandroln.openedboard.MoveTickets
import com.github.sandroln.openedboard.ShowTicketDetails
import com.github.sandroln.openedboard.TicketUi

internal class TicketsAdapter(
    private val interaction: TicketInteractions,
) : RecyclerView.Adapter<TicketViewHolder>(), Mapper.Unit<List<TicketUi>> {

    private val ticketsUi = mutableListOf<TicketUi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TicketViewHolder(
        interaction,
        LayoutInflater.from(parent.context).inflate(R.layout.ticket, parent, false)
    )

    override fun getItemCount() = ticketsUi.size

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) =
        holder.bind(ticketsUi[position])

    override fun map(source: List<TicketUi>) {
        val diff = Diff(ticketsUi, source)
        val result = DiffUtil.calculateDiff(diff)
        ticketsUi.clear()
        ticketsUi.addAll(source)
        result.dispatchUpdatesTo(this)
    }
}

internal class TicketViewHolder(
    private val interaction: TicketInteractions,
    view: View
) : RecyclerView.ViewHolder(view) {

    private val colorView = itemView.findViewById<View>(R.id.colorView)
    private val title = itemView.findViewById<TextView>(R.id.titleTextView)
    private val assignee = itemView.findViewById<TextView>(R.id.assigneeTextView)
    private val left = itemView.findViewById<View>(R.id.leftButton)
    private val right = itemView.findViewById<View>(R.id.rightButton)

    fun bind(ticketUi: TicketUi) = with(ticketUi) {
        map(colorView, title, assignee)
        map(left, right)
        left.setOnClickListener {
            interaction.moveLeft(ticketUi)
        }
        right.setOnClickListener {
            interaction.moveRight(ticketUi)
        }
        itemView.setOnClickListener {
            showDetails(interaction)
        }
        itemView.setOnLongClickListener {
            val data = interaction.toString(ticketUi.toSerializable())
            val item = ClipData.Item(data)

            val dragData = ClipData(
                data,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )

            val myShadow = View.DragShadowBuilder(itemView)

            itemView.startDragAndDrop(dragData, myShadow, null, 0)
            true
        }
    }
}

internal interface MoveCallback {
    fun change(adapterColumn: Column, item: String)
}

internal interface TicketInteractions :
    MoveTickets<TicketUi>, MoveCallback, ShowTicketDetails, Serialization.From

private class Diff(
    private val old: List<TicketUi>,
    private val new: List<TicketUi>
) : DiffUtil.Callback() {

    override fun getOldListSize() = old.size
    override fun getNewListSize() = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        old[oldItemPosition].same(new[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        old[oldItemPosition] == new[newItemPosition]
}