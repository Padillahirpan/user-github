package com.irpn.usersgithub.di.modules

object Modules {
    fun getAll() = listOf(
        ViewModelModules.viewModelModules,
        NetworkModules.networkModules,
        RepositoryModules.repoModules,
        CoroutineModules.dispatcherModule,
        ApplicationModules.applicationModules,
        FeatureApiModule.modules
    )
}