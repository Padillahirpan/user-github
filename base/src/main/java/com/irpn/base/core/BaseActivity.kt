package com.irpn.base.core

import android.os.Bundle
import androidx.lifecycle.Observer


abstract class BaseActivity<VM : BaseViewModel> : PlainActivity() {

    abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setObserver()
    }

    private fun setObserver() {

        viewModel.forceLogout.observe(
            this,
            Observer<Int> {
                //forceLogout()
            })

    }

/*
    private fun forceLogout() {
        startActivity(
            Intent(Intent.ACTION_VIEW).setClassName(packageName!!,"com.she.sehatq.view.login.LoginActivity")
                .putExtra(Constants.HIDE_SKIP_BUTTON, true)
        )
        finish()

    }
*/

}