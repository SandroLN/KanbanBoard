package com.github.sandroln.kanbanboard.board.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.github.sandroln.kanbanboard.R
import com.github.sandroln.kanbanboard.core.ProvideViewModel

class BoardToolbar : FrameLayout {

    private lateinit var viewModel: BoardToolbarViewModel
    private val titleTextView: TextView
    private val settingsButton: View
    private val backButton: View

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflate(context, R.layout.board_toolbar, this)
        titleTextView = findViewById(R.id.boardNameTextView)
        backButton = findViewById(R.id.backButton)
        settingsButton = findViewById(R.id.settingsButton)
        backButton.setOnClickListener {
            viewModel.goBack()
        }
        settingsButton.setOnClickListener {
            viewModel.showSettings()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewModel = (context.applicationContext as ProvideViewModel).viewModel(
            findViewTreeViewModelStoreOwner()!!,
            BoardToolbarViewModel::class.java
        )
        viewModel.observe(findViewTreeLifecycleOwner()!!) {
            it.show(titleTextView, settingsButton)
        }
    }
}