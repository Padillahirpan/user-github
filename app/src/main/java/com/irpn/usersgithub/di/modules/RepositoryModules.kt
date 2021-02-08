package com.irpn.usersgithub.di.modules

import com.irpn.home.repository.HomeRepository
import com.irpn.home.repository.HomeRepositoryImpl
import org.koin.dsl.module

class RepositoryModules {
    companion object {
        val repoModules = module(override = true) {
            single<HomeRepository> { HomeRepositoryImpl(get()) }
        }
    }
}