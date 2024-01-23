package com.github.sandroln.kanbanboard.profile

import com.github.sandroln.core.Core
import com.github.sandroln.core.Module
import com.github.sandroln.kanbanboard.profile.presentation.ProfileCommunication
import com.github.sandroln.kanbanboard.profile.presentation.ProfileViewModel

class ProfileModule(private val core: Core) : Module<ProfileViewModel> {

    override fun viewModel() = ProfileViewModel(
        core.provideMyUser(),
        ProfileCommunication.Base(),
        core.navigation()
    )
}