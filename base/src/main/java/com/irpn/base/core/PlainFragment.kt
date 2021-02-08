package com.irpn.base.core

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.irpn.base.R
import com.irpn.base.extensions.getColorFromRes
import timber.log.Timber

abstract class PlainFragment : Fragment() {

    companion object {

        const val ALERT_ERROR = "alert_error"
    }

    @LayoutRes
    abstract fun getLayoutRes(): Int

    @ColorRes
    open fun statusBarColor(): Int = R.color.default_status_bar_color

    open fun lightColorIcon(): Boolean = false

    fun fragmentTag(): String = this::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.tag(fragmentTag()).d("fragment on view created")
        return inflater.inflate(getLayoutRes(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag(fragmentTag()).d("fragment on view created")
        changeStatusBarColor()
        changeStatusBarIconColor()
        viewInitialization(view,savedInstanceState)
        viewInitialization(view)
        onPreparationFinished(view)
    }

    override fun onPause() {
        super.onPause()
        Timber.tag(fragmentTag()).d("fragment on pause")
    }

    override fun onStart() {
        super.onStart()
        Timber.tag(fragmentTag()).d("fragment on start")
    }

    override fun onStop() {
        super.onStop()
        Timber.tag(fragmentTag()).d("fragment on stop")
    }

    override fun onResume() {
        super.onResume()
        Timber.tag(fragmentTag()).d("fragment on resume")
    }

//    protected fun showLoading(isShow: Boolean) {
//        (activity as PlainActivity?)?.showLoading(isShow)
//    }

    fun changeStatusBarColor(@ColorRes barColor: Int = statusBarColor()) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
        activity?.window?.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor().let { statusBarColor = context.getColorFromRes(barColor) }
        }
    }

    fun showBackButton() {
        (activity as BaseActivity<*>).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun showBackButton(show: Boolean) {
        Timber.tag(fragmentTag()).d("fragment on back pressed")
        (activity as BaseActivity<*>).supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }

    fun hideBackButton() {
        (activity as BaseActivity<*>).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    fun setToolBarTitle(title: String) {
        (activity as BaseActivity<*>).supportActionBar?.title = title
    }

    private fun changeStatusBarIconColor() {
        activity?.window?.decorView?.rootView?.systemUiVisibility =
            if (lightColorIcon()) 0 else 8192
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.tag(fragmentTag()).d("fragment on destroy view")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.tag(fragmentTag()).d("fragment on destroy")
    }


    /**
     * This method is called after view has been created.
     * This method should be used to initialize all views that are needed to be created (and recreated after fragment is reattached)
     * @param view The root view of the fragment
     */
    open fun viewInitialization(view: View) {}

    open fun viewInitialization(view: View, savedInstanceState: Bundle?) {}

    /**
     * This method is called after viewInitialization(view) is finished
     * @param view The root view of the fragment
     */
    open fun onPreparationFinished(view: View) {}

    open fun onNetworkRequestFailure(errorCode: Int, errorMap: Map<String, String>) {}
}