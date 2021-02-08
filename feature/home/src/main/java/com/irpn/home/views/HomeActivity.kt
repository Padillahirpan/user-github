package com.irpn.home.views

import android.os.Bundle
import com.irpn.base.contract.FragmentNavigation
import com.irpn.base.core.PlainActivity
import com.irpn.users.R

class HomeActivity : PlainActivity(), FragmentNavigation {

    override fun getLayoutRes(): Int = R.layout.activity_user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        startWithClearBackStack(
            this@HomeActivity,
            R.id.fragmentLayout,
            HomeFragment.newInstance()
        )
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count > 0) {
            supportFragmentManager.popBackStack()
        } else finish()
    }


}
