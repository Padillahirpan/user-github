package com.irpn.base.core

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.irpn.base.R
import com.irpn.base.exception.RequestException
import com.irpn.base.extension.errorCode
import com.irpn.base.model.DataState
import com.irpn.base.model.Result
import com.irpn.base.utils.Constants
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

open class BaseViewModel(private val app: Application, private val sharedPref: SharedPreferences) :
    AndroidViewModel(app) {

    val forceLogout = MutableLiveData<Int>()

    open fun doOnViewAttached() {}

    open fun onConnectedToNetwork(isConnected: Boolean) {}

    override fun onCleared() {
        super.onCleared()
    }

    protected fun <T> Result.Failure<*>.toFailureDataState() = when (exception) {
        is UnknownHostException,
        is SocketException,
        is SocketTimeoutException,
        is TimeoutException,
        is ConnectException -> DataState.Failure(
            Constants.NO_INTERNET_CODE,
            app.getString(R.string.no_connection).orEmpty(),
            emptyMap(),
            null
        )
        is RequestException -> {
            if (exception.errorCode == Constants.UNAUTHORISED_CODE) {
                //deleteUser()
                forceLogout.postValue(0)
            }

            Timber.e("AbstractHomeFragment, ${exception.statusCode} and the status code: ${exception.title} dan oke ${exception.errorMap} dan")

            DataState.Failure(
                exception.errorCode,
                if (exception.localizedMessage.isEmpty()) app.getString(R.string.default_error_network) else exception.localizedMessage,
                exception.errorMap,
                exception.getLocalizedTitle()

            )

        }
        else -> DataState.Failure<T>(
            exception.errorCode,
            app.getString(R.string.default_error_network),
            emptyMap(),
            null
        )
    }
}