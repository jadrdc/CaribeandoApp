package com.agusteam.caribeando.presenter.home.viewmodel

import com.agusteam.caribeando.core.base.GenericViewModel
import com.agusteam.caribeando.presenter.home.state.HomeOption
import com.agusteam.caribeando.presenter.home.state.HomeState

class HomeViewModel :
    GenericViewModel<HomeState, HomeViewModel.HomeEvent>(HomeState()) {

    private fun changeHomeTab(value: HomeOption) {
        updateState {
            copy(currentNavigationOption = value)
        }
    }

    fun handleEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.ChangeHomeTab -> {
                changeHomeTab(event.option)
            }
        }
    }

    sealed interface HomeEvent {
        data class ChangeHomeTab(val option: HomeOption) : HomeEvent
    }
}