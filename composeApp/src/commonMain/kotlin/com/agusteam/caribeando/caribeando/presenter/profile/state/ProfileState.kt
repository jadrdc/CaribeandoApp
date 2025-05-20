package com.agusteam.caribeando.presenter.profile.state

import com.agusteam.caribeando.core.base.ViewModelState
import com.agusteam.caribeando.domain.models.ErrorModel

data class ProfileState(
    val isLoading: Boolean = false,
    val userProfileName: String = "",
    val name: String = "",
    val lastname: String = "",
    val email: String = "",
    val phone: String = "",
    val errorModel: ErrorModel? = null
) : ViewModelState {
    val fullName: String
        get() = "$name $lastname"
}
