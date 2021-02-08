package com.irpn.home.abstract

import android.view.View
import com.irpn.base.common.EventObserver
import com.irpn.base.core.BaseFragment
import com.irpn.base.model.DataState
import com.irpn.home.model.GithubUserResponse
import com.irpn.home.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

abstract class AbstractHomeFragment : BaseFragment<HomeViewModel>() {

    var listItems = mutableListOf<GithubUserResponse?>()
    private var keywordSearch: String = ""
    var isLoading = false
    var page = 1
    var nextPage = false
    var lastTextChanged: Long = 0
    var isSearch = false


    override val viewModel: HomeViewModel by sharedViewModel()

    override fun viewInitialization(view: View) {
        super.viewInitialization(view)

        observeDataChange()
    }

    private fun observeDataChange(){
        viewModel.initialDataLiveData.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                is DataState.SuccessList -> {
                    prepareData(it.data)
                }
                is DataState.Failure -> {
                    updateEmpty(it.errorMessage)
                    Timber.e("xyz AbstractHomeFragment, ${it.errorMessage} and the status code: ${it.errorCode}")
                }
            }
        })
    }

    fun loadMore() {
        viewModel.searchUser(keywordSearch, page)
    }

    fun getTotalPage(): Int = viewModel.totalPage

    fun clearTotal() {
        viewModel.totalPage = 0
    }

    fun searchUserPage(keyword: String? = "") {
        listItems.clear()
        keywordSearch = keyword.toString()
        page = 1
        isSearch = true
        clearTotal()
        lastTextChanged = System.currentTimeMillis()

        viewModel.searchUser(keywordSearch)
    }

    private fun prepareData(data: List<GithubUserResponse>) {
        updateData(data)
    }

    abstract fun updateData(data: List<GithubUserResponse>)
    abstract fun clearList()
    abstract fun updateEmpty(errorMessage: String)

}
