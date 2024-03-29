package com.github.sandroln.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner

interface ProvideViewModel {

    fun <T : ViewModel> viewModel(owner: ViewModelStoreOwner, className: Class<T>): T
}