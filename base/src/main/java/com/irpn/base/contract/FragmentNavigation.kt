package com.irpn.base.contract

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import com.irpn.base.core.BaseFragment
import com.irpn.base.core.PlainActivity
import com.irpn.base.core.PlainFragment

interface FragmentNavigation {

    fun startWithClearBackStack(
        activity: PlainActivity, @IdRes contentId: Int,
        newFragment: PlainFragment
    ) {
        val destinationFragment: Fragment?
        with(activity.supportFragmentManager) {
            destinationFragment = findFragmentByTag(newFragment.fragmentTag())
            destinationFragment?.arguments = newFragment.arguments
        }
        activity.supportFragmentManager.transaction {
            setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            for (fragment in activity.supportFragmentManager.fragments) {
                detach(fragment)
            }
            if (destinationFragment != null) attach(destinationFragment)
            else {
                replace(contentId, newFragment, newFragment.fragmentTag())
            }
        }
    }

    fun startPlainFragment(
        activity: PlainActivity, @IdRes contentId: Int,
        plainFragment: PlainFragment,
        addToBackStack: Boolean = true,
        withAnimation: Boolean = true,
        isAddFragment: Boolean = false
    ) {
        activity.supportFragmentManager.transaction {
            if (withAnimation) setCustomAnimations(
                android.R.animator.fade_in,
                android.R.animator.fade_out
            )
            if (isAddFragment) {
                add(contentId, plainFragment, plainFragment.fragmentTag())
            } else {
                replace(contentId, plainFragment, plainFragment.fragmentTag())
            }
            if (addToBackStack) addToBackStack(plainFragment.fragmentTag())
        }
    }

    fun startFragment(
        activity: PlainActivity, @IdRes contentId: Int,
        newFragment: BaseFragment<*>,
        addToBackStack: Boolean = true,
        withAnimation: Boolean = true,
        isAddFragment: Boolean = false
    ) {
        startPlainFragment(
            activity,
            contentId,
            newFragment,
            addToBackStack,
            withAnimation,
            isAddFragment
        )
    }
}