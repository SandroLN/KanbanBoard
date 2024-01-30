package com.github.sandroln.kanbanboard.core

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.github.sandroln.core.ProvideViewModel

class App : Application(), ProvideViewModel {

    private lateinit var viewModelsFactory: ViewModelsFactory

    override fun onCreate() {
        super.onCreate()
        val make = MakeDependencies.Base(this)
        viewModelsFactory = ViewModelsFactory(make.dependencies())
    }

    override fun <T : ViewModel> viewModel(owner: ViewModelStoreOwner, className: Class<T>): T =
        ViewModelProvider(owner, viewModelsFactory)[className]
}