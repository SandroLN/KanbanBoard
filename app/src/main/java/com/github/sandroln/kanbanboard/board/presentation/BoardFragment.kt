package com.github.sandroln.kanbanboard.board.presentation

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.sandroln.kanbanboard.R
import com.github.sandroln.kanbanboard.core.BaseFragment

class BoardFragment : BaseFragment<BoardViewModel>(R.layout.fragment_board) {

    override val viewModelClass = BoardViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //region init views
        val progressBar = view.findViewById<View>(R.id.progressBar)
        val errorView = view.findViewById<View>(R.id.errorView)
        val errorTextView = view.findViewById<TextView>(R.id.errorTextView)
        val tryAgainButton = view.findViewById<View>(R.id.tryAgainButton)
        val todoRecyclerView = view.findViewById<RecyclerView>(R.id.todoRecyclerView)
        val inProgressRecyclerView = view.findViewById<RecyclerView>(R.id.inProgressRecyclerView)
        val doneRecyclerView = view.findViewById<RecyclerView>(R.id.doneRecyclerView)
        val createTicketButton = view.findViewById<View>(R.id.createTicketButton)

        val todoAdapter = TicketsAdapter(viewModel)
        todoRecyclerView.setOnDragListener(DragListener(Column.Todo, viewModel))
        todoRecyclerView.adapter = todoAdapter

        val inProgressAdapter = TicketsAdapter(viewModel)
        inProgressRecyclerView.setOnDragListener(DragListener(Column.InProgress, viewModel))
        inProgressRecyclerView.adapter = inProgressAdapter

        val doneAdapter = TicketsAdapter(viewModel)
        doneRecyclerView.setOnDragListener(DragListener(Column.Done, viewModel))
        doneRecyclerView.adapter = doneAdapter
        //endregion
        createTicketButton.setOnClickListener {
            viewModel.createTicket()
        }
        tryAgainButton.setOnClickListener {
            viewModel.reload()
        }

        viewModel.observe(this) {
            it.map(errorView, errorTextView, progressBar)
        }
        viewModel.observeTodoColumn(this) {
            todoAdapter.map(it)
        }
        viewModel.observeInProgressColumn(this) {
            inProgressAdapter.map(it)
        }
        viewModel.observeDoneColumn(this) {
            doneAdapter.map(it)
        }
    }

    private inner class DragListener(
        private val column: Column,
        private val moveCallback: MoveCallback
    ) : View.OnDragListener {

        override fun onDrag(v: View, event: DragEvent): Boolean {
            if (event.action == DragEvent.ACTION_DROP) {
                val item: ClipData.Item = event.clipData.getItemAt(0)
                val dragData = item.text.toString()
                moveCallback.change(column, dragData)
            }
            return true
        }
    }
}