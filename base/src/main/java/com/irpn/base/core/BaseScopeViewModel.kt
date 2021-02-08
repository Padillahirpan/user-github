package com.irpn.base.core

import android.app.Application
import android.content.SharedPreferences
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseScopeViewModel(app: Application, sharedPref: SharedPreferences) : BaseViewModel(app, sharedPref),
    CoroutineScope {

    private val failedJobIdList = mutableSetOf<String>()

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override fun onCleared() {
        super.onCleared()
        failedJobIdList.clear()
        job.cancel()
    }

    private fun addJobId(jobId: String) {
        Timber.tag(jobId).d("Added to stack")
        failedJobIdList.add(jobId)
    }

    private fun removeJobId(jobId: String) {
        Timber.tag(jobId).d("Removed from stack")
        if (!failedJobIdList.isNullOrEmpty())
            failedJobIdList.remove(jobId)
    }

    protected fun CoroutineScope.launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        doOnComplete: (throwable: Throwable?) -> Unit = {},
        id: String,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return launch(context, start, block)
            .apply {
                invokeOnCompletion {
                    if (it != null) addJobId(id)
                    else removeJobId(id)

                    doOnComplete(it)
                }
            }
    }

    protected fun <T> CoroutineScope.async(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        doOnComplete: (throwable: Throwable?) -> Unit = {},
        id: String,
        block: suspend CoroutineScope.() -> T
    ): Deferred<T> {
        return async(context, start, block)
            .apply {
                invokeOnCompletion {
                    Timber.tag("Job $id Completed").d(it)

                    if (it != null) addJobId(id)
                    else removeJobId(id)

                    doOnComplete(it)
                }
            }
    }

    protected fun CoroutineScope.addToFailedJobStack(throwable: Throwable? = null) {
        Timber.tag("Add Job To Failed Stack").d(throwable)
        coroutineContext.cancel()
    }

    override fun onConnectedToNetwork(isConnected: Boolean) {
        super.onConnectedToNetwork(isConnected)
        if (isConnected)
            resumeFailedJob(failedJobIdList)
    }

    open fun resumeFailedJob(jobIdList: Iterable<String>) {}
}