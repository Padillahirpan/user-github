package com.irpn.usersgithub.di.modules

import com.irpn.base.coroutine.DefaultDispatcherProvider
import com.irpn.base.coroutine.DispatcherProvider
import org.koin.dsl.module

object CoroutineModules {

    val dispatcherModule = module(override = true)  {
        single<DispatcherProvider> {
            DefaultDispatcherProvider()
        }
    }
}