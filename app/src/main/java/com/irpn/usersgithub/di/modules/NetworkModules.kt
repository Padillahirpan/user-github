package com.irpn.usersgithub.di.modules

import com.irpn.base.network.NetworkBuilder
import com.irpn.home.api.HomeApi
import org.koin.dsl.module

class NetworkModules {
    companion object {
        val networkModules = module(override = true) {
            single<HomeApi> {
                NetworkBuilder.create(
                    com.irpn.base.BuildConfig.BASE_URL,
                    HomeApi::class.java,
                    get()
                )
            }
        }
    }
}