package com.github.sandroln.kanbanboard.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseFragment<T : ViewModel>(layoutId: Int) : Fragment(layoutId) {

    protected lateinit var viewModel: T
    protected abstract val viewModelClass: Class<T>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as ProvideViewModel).viewModel(this, viewModelClass)
    }
}