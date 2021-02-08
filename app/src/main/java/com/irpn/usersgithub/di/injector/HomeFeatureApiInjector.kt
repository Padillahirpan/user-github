package com.irpn.usersgithub.di.injector

import android.content.Context
import android.content.Intent
import com.irpn.home.featureapi.HomeFeatureApi
import com.irpn.usersgithub.views.splash.SplashActivity

class HomeFeatureApiInjector : HomeFeatureApi {
    override fun navigateToHome(context: Context, intent: Intent) = with(context) {
        startActivity(
            intent.setClass(this, SplashActivity::class.java)
        )
    }
}