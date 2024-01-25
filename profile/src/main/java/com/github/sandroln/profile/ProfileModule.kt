package com.github.sandroln.profile

import com.github.sandroln.core.Core
import com.github.sandroln.core.Module
import com.github.sandroln.profile.presentation.ProfileCommunication
import com.github.sandroln.profile.presentation.ProfileViewModel

internal class ProfileModule(private val core: Core) : Module<ProfileViewModel> {

    override fun viewModel() = ProfileViewModel(
        core.provideMyUser(),
        ProfileCommunication.Base(),
        core.navigation()
    )
}