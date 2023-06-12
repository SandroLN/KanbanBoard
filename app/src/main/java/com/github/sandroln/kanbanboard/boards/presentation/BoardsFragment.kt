package com.github.sandroln.kanbanboard.boards.presentation

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.sandroln.kanbanboard.R
import com.github.sandroln.kanbanboard.core.BaseFragment

class BoardsFragment : BaseFragment<BoardsViewModel>(R.layout.fragment_boards) {

    override val viewModelClass = BoardsViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = BoardsAdapter(viewModel)
        view.findViewById<RecyclerView>(R.id.boardsRecyclerView).adapter = adapter
        view.findViewById<View>(R.id.settingsButton).setOnClickListener {
            viewModel.showProfile()
        }
        view.findViewById<View>(R.id.createButton).setOnClickListener {
            viewModel.createBoard()
        }
        viewModel.observe(this) {
            it.show(adapter)
        }

        viewModel.init(savedInstanceState == null)
    }
}