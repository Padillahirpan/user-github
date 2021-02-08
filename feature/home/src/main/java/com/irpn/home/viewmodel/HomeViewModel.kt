package com.irpn.home.viewmodel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.irpn.base.common.Event
import com.irpn.base.core.BaseScopeViewModel
import com.irpn.base.coroutine.DispatcherProvider
import com.irpn.base.extension.awaitAndGet
import com.irpn.base.model.DataState
import com.irpn.base.model.Result
import com.irpn.home.model.GithubUserResponse
import com.irpn.home.repository.HomeRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(
    application: Application,
    private val sharedPreferences: SharedPreferences,
    private val homeRepository: HomeRepository,
    private val dispatcherProvider: DispatcherProvider
): BaseScopeViewModel(application, sharedPreferences) {

    private val _initialDataLiveData by lazy { MutableLiveData<Event<DataState<GithubUserResponse>>>() }
    val initialDataLiveData: LiveData<Event<DataState<GithubUserResponse>>> by lazy { _initialDataLiveData }

    var totalPage = 0
    var nextPage = false

    fun searchUser(query: String, page: Int = 1) {
        Timber.d("xyz this is searchUser: $page and query: $query and $totalPage")
        launch(dispatcherProvider.main()) {
            when (val result = homeRepository.getGithubUsersAsync(query, page.toString(), "10" ).awaitAndGet()) {
                is Result.Success -> {
                    result.body?.let {
                        it.total_count.let { tot ->
                            totalPage = tot
                        }
                        val itemCount = it.result.count()
//                        nextPage = (result.totalCount!! > totalPage && result.totalCount > perPage)
                        Event(DataState.SuccessList(it.result))
                    }.run(_initialDataLiveData::postValue)
                }
                is Result.Failure -> {
                    Event(result.toFailureDataState<GithubUserResponse>()).run(
                        _initialDataLiveData::postValue
                    )
                }
            }
        }
    }
}