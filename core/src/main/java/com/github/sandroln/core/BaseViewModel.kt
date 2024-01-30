package com.github.sandroln.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


abstract class BaseViewModel(private val dispatchersList: DispatchersList) : ViewModel(),
    HandleAsync {

    override fun <T : Any> handle(
        block: suspend () -> T,
        ui: (T) -> Unit
    ) = viewModelScope.launch(dispatchersList.io()) {
        val result = block.invoke()
        withContext(dispatchersList.ui()) {
            ui.invoke(result)
        }
    }
}

interface HandleAsync {

    fun <T : Any> handle(
        block: suspend () -> T,
        ui: (T) -> Unit
    ): Job
}