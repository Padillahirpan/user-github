package com.irpn.home.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.irpn.base.extension.loadCornerImage
import com.irpn.home.model.GithubUserResponse
import com.irpn.users.R
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter(
    var items: MutableList<GithubUserResponse?>,
    var onClick: UserListener
): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var tempPosition = 0
    private val ITEM_CONTENT = 1
    private val ITEM_LOADING = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ITEM_CONTENT -> ItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false),
                onClick
            )
            else -> LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            )

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position]?.let { item ->
            when (holder) {
                is ItemViewHolder -> holder.bind(item)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.let { ITEM_CONTENT } ?: ITEM_LOADING
    }

    fun getItem(position: Int): GithubUserResponse? = items[position]

    fun onLoadMore() {
        items.add(null)
        tempPosition = itemCount - 1
        notifyDataSetChanged()
    }

    fun onFinishLoadMore() {
        if (tempPosition < items.size){
            items.removeAt(tempPosition)
        }
    }

    inner class ItemViewHolder(itemView: View, var onClick: UserListener) :
        ViewHolder(itemView) {
        fun bind(item: GithubUserResponse?) {
            with(itemView) {
                tvUserName.text = item?.login
                tvUserType.text = item?.type
                ivUserImage.loadCornerImage(
                    item?.avatar_url,
                    radius = 10
                )
                setOnClickListener { item?.let { onClick.onItemSelected(it) } }
            }
        }

    }

    inner class LoadingViewHolder(itemView: View) : ViewHolder(itemView)

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface UserListener {
        fun onItemSelected(item: GithubUserResponse)
    }
}