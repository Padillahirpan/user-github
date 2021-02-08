package com.irpn.home.views

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.irpn.base.contract.FragmentNavigation
import com.irpn.base.extension.aftertextChanged
import com.irpn.base.extension.invisible
import com.irpn.base.extension.showKeyboard
import com.irpn.base.extension.visible
import com.irpn.base.extensions.showLongToast
import com.irpn.base.extensions.showShortToast
import com.irpn.base.utils.CheckScrollListener
import com.irpn.home.abstract.AbstractHomeFragment
import com.irpn.home.model.GithubUserResponse
import com.irpn.users.R
import com.irpn.home.views.adapter.UserAdapter
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


class HomeFragment : AbstractHomeFragment(),
    FragmentNavigation, UserAdapter.UserListener {

    private val schoolAdapter by lazy { UserAdapter(mutableListOf(), onClick = this) }

    override fun getLayoutRes(): Int = R.layout.fragment_user

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun viewInitialization(view: View) {
        super.viewInitialization(view)
        setupUI()
        setupAdapter()
    }

    private fun setupUI() {
        swipeRefresh.isRefreshing = false
        edtSearch.aftertextChanged {
            swipeRefresh.isRefreshing = true
            if (it.isNotEmpty()) {
                ivClose.visible()
                searchUserPage(it)
            } else {
                clearList()
                isSearch = false
                ivClose.invisible()
            }
        }

        swipeRefresh.setOnRefreshListener {
            clearList()
        }

        ivClose.setOnClickListener {
            edtSearch.setText("")
            isSearch = false
        }
        edtSearch.setOnTouchListener { _, _ ->
            edtSearch.showKeyboard()
            true
        }

        ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setupAdapter() {
        with(rvSchool) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                false
            ).also {
                adapter = schoolAdapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val countItem = (layoutManager as LinearLayoutManager).itemCount
                        val lastVisiblePosition =
                            (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                        val isLastPosition = countItem.minus(1) == lastVisiblePosition
                        if (!isLoading && isLastPosition && this@HomeFragment.listItems.size != getTotalPage()) {
                            isLoading = true
                            page++
                            schoolAdapter.onLoadMore()
                            GlobalScope.launch(Dispatchers.Main) {
                                try {
                                    delay(800)
                                    loadMore()
                                } catch (t: Throwable) {
                                    Timber.d("HomeFragment -> ${t.message}")
                                }
                            }
                        }
                    }
                })
            }
        }
    }

    override fun updateData(data: List<GithubUserResponse>) {
        if (swipeRefresh.isRefreshing) swipeRefresh.isRefreshing = false

        if (data.isEmpty()) ctEmptyState.visibility = View.VISIBLE
        else ctEmptyState.visibility = View.GONE

        tvErrorMessage.text = getString(R.string.search_empty)

        if (isLoading) {
            schoolAdapter.onFinishLoadMore()
            isLoading = false
        }

        Timber.d("xyz this is updateDate: $isSearch and isLoading $isLoading")
        if (isSearch) {
            listItems.clear()
            listItems.addAll(data)
        } else {
            listItems.addAll(data)
        }

        with(schoolAdapter) {
            items = listItems
            notifyDataSetChanged()
        }
    }

    override fun clearList() {
        if (swipeRefresh.isRefreshing) swipeRefresh.isRefreshing = false

        ctEmptyState.visibility = View.GONE

        if (isLoading) {
            schoolAdapter.onFinishLoadMore()
            isLoading = false
        }
        clearTotal()
        listItems.clear()
        with(schoolAdapter) {
            items = listItems
            notifyDataSetChanged()
        }
    }

    override fun updateEmpty(errorMessage: String) {
        if (swipeRefresh.isRefreshing) swipeRefresh.isRefreshing = false

        ctEmptyState.visibility = View.VISIBLE

        tvErrorMessage.text = errorMessage

        if (isLoading) {
            schoolAdapter.onFinishLoadMore()
            isLoading = false
        }
        clearTotal()
        listItems.clear()
        with(schoolAdapter) {
            items = listItems
            notifyDataSetChanged()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment().apply {
        }
    }

    override fun onItemSelected(item: GithubUserResponse) {
        context?.showShortToast(item.login)
        Timber.d("OnClicked: "+item.login)
    }
}
