package com.irpn.usersgithub.di.modules

import com.irpn.home.featureapi.HomeFeatureApi
import com.irpn.usersgithub.di.injector.HomeFeatureApiInjector
import org.koin.dsl.module

object FeatureApiModule {
    val modules = module {
        single<HomeFeatureApi> { HomeFeatureApiInjector() }
    }

}