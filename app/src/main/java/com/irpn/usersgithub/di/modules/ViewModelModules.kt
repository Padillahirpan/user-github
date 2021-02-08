package com.irpn.usersgithub.di.modules

import com.irpn.home.viewmodel.HomeViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel


class ViewModelModules {
    companion object {
        val viewModelModules = module(override = true) {
            viewModel { HomeViewModel(get(), get(), get(), get()) }
        }
    }
}