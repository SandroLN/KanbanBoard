package com.github.sandroln.kanbanboard.boards.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.github.sandroln.kanbanboard.R

class MyInvitationsView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private lateinit var viewModel: MyInvitationsViewModel


    init {
        inflate(context, R.layout.invitations_to_board, this)
        visibility = View.GONE
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewModel = (context.applicationContext as com.github.sandroln.core.ProvideViewModel).viewModel(
            findViewTreeViewModelStoreOwner()!!,
            MyInvitationsViewModel::class.java
        )
        val adapter = InvitationsAdapter(viewModel)
        findViewById<RecyclerView>(R.id.invitationsRecyclerView).adapter = adapter
        viewModel.observe(findViewTreeLifecycleOwner()!!) {
            adapter.map(it)
            visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
        }
    }
}