package com.irpn.usersgithub.di.modules

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.irpn.usersgithub.R
import org.koin.dsl.module

class ApplicationModules {
    companion object {
        val applicationModules = module(override = true){
            // application level modules if any
            single {
                get<Application>().run {
                    getSharedPreferences(
                        getString(R.string.app_name),
                        MODE_PRIVATE
                    )
                }
            }
        }
    }
}