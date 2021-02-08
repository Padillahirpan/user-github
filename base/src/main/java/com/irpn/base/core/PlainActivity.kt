package com.irpn.base.core

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.irpn.base.R
import com.irpn.base.extension.hideKeyboard
import com.irpn.base.utils.PageLoadingProgress
import timber.log.Timber

abstract class PlainActivity : AppCompatActivity() {
    protected val loadingDialog by lazy { PageLoadingProgress(this) }
    val TAG by lazy { this::class.java.simpleName }

    @LayoutRes
    abstract fun getLayoutRes(): Int

    @ColorRes
    open fun statusBarColor(): Int = R.color.colorPrimary

    open fun lightColorIcon(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
        Timber.tag(TAG).d("Activity on create")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) changeStatusBarColor()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) changeStatusBarIconColor()
    }

    override fun onStart() {
        super.onStart()
        Timber.tag(TAG).d("Activity on start")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.tag(TAG).d("Activity on restart")
    }

    override fun onResume() {
        super.onResume()
        Timber.tag(TAG).d("Activity on resume")
    }

    override fun onStop() {
        super.onStop()
        Timber.tag(TAG).d("Activity on stop")
    }

    protected fun Context.getColorFromRes(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

    protected fun Context.getDimenFromRes(@DimenRes res: Int) = getTypedValue(res).float

    @SuppressWarnings("InlinedApi", "newApi")
    private fun changeStatusBarColor() {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = getColorFromRes(statusBarColor())
        }
    }

    private fun changeStatusBarIconColor() {
        window.decorView.rootView.systemUiVisibility = if (lightColorIcon()) 0 else 8192
    }

    private fun Context.getTypedValue(res: Int): TypedValue {
        return TypedValue().apply {
            resources.getValue(res, this, true)
        }
    }
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        when (item?.itemId) {
//            android.R.id.home -> onBackPressed()
//        }
//        return true
//    }

    override fun onBackPressed() {
        Timber.tag(TAG).d("Activity on back pressed")
        //if (!loadingDialog.isVisible(this)) super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.tag(TAG).d("Activity on destroy")
        //hideKeyboard()
    }

    override fun onPause() {
        super.onPause()
        Timber.tag(TAG).d("Activity on pause")
    }

    fun showLoading(isShow: Boolean) {
        if (isShow) {
            with(loadingDialog) {
                showFromActivity(this@PlainActivity)
                requestFocus()
            }
            hideKeyboard()
        } else loadingDialog.hideFromActivity(this)
    }

    fun changeStatusBarColor(@ColorRes barColor: Int = statusBarColor()) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = getColorFromRes(barColor)
        }
    }

    fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun setToolBarTitle(title:String){
        supportActionBar?.title = title
    }

    fun setToolBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }
}