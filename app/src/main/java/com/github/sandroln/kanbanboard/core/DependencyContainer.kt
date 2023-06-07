package com.github.sandroln.kanbanboard.core

import androidx.lifecycle.ViewModel
import com.github.sandroln.kanbanboard.login.LoginModule
import com.github.sandroln.kanbanboard.login.presentation.LoginViewModel
import com.github.sandroln.kanbanboard.main.MainModule
import com.github.sandroln.kanbanboard.main.MainViewModel
import com.github.sandroln.kanbanboard.profile.ProfileModule
import com.github.sandroln.kanbanboard.profile.presentation.ProfileViewModel

interface DependencyContainer {

    fun module(className: Class<out ViewModel>): Module<out ViewModel>

    class Error : DependencyContainer {

        override fun module(className: Class<out ViewModel>): Module<out ViewModel> =
            throw IllegalStateException("unknown classname $className")
    }

    class Base(
        private val core: Core,
        private val dependencyContainer: DependencyContainer = Error()
    ) : DependencyContainer {
        override fun module(className: Class<out ViewModel>): Module<out ViewModel> =
            when (className) {
                MainViewModel::class.java -> MainModule(core)
                LoginViewModel::class.java -> LoginModule(core)
                ProfileViewModel::class.java -> ProfileModule(core)
                else -> dependencyContainer.module(className)
            }
    }
}