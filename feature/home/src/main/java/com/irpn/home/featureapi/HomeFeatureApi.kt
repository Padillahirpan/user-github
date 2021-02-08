package com.irpn.home.featureapi

import android.content.Context
import android.content.Intent


interface HomeFeatureApi {
    fun navigateToHome(context: Context, intent: Intent)
}