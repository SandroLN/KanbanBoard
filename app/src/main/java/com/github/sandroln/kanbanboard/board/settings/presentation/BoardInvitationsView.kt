package com.github.sandroln.kanbanboard.board.settings.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.github.sandroln.core.ProvideViewModel
import com.github.sandroln.kanbanboard.R
import com.github.sandroln.kanbanboard.ticket.create.presentation.AssignUser

class BoardInvitationsView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private lateinit var viewModel: BoardInvitationViewModel

    private val membersAdapter = BoardSettingsMembersAdapter(AssignUser.Empty)

    init {//todo duplicated_invitations_to_board problem copy pasted other module
        inflate(context, R.layout.duplicated_invitations_to_board, this)
        findViewById<RecyclerView>(R.id.invitationsRecyclerView).adapter = membersAdapter
        visibility = View.GONE
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewModel = (context.applicationContext as ProvideViewModel).viewModel(
            findViewTreeViewModelStoreOwner()!!,
            BoardInvitationViewModel::class.java
        )
        viewModel.observe(findViewTreeLifecycleOwner()!!) {
            membersAdapter.map(it)
            visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
        }
    }
}