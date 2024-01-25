package com.github.sandroln.profile

import androidx.lifecycle.ViewModel
import com.github.sandroln.core.Core
import com.github.sandroln.core.DependencyContainer
import com.github.sandroln.core.Module
import com.github.sandroln.profile.presentation.ProfileViewModel

class ProfileDependencyContainer(
    private val core: Core,
    private val other: DependencyContainer
) : DependencyContainer {

    override fun module(className: Class<out ViewModel>): Module<out ViewModel> =
        if (className == ProfileViewModel::class.java)
            ProfileModule(core)
        else
            other.module(className)
}