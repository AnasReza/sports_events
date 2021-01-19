package com.sportseventmanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.sportseventmanagement.R
import com.sportseventmanagement.`interface`.BaseItem
import com.sportseventmanagement.`interface`.BaseViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.notification_head.*
import kotlinx.android.synthetic.main.notification_item.*

class BaseListAdapter(private val itemClickCallback: ((BaseItem) -> Unit)?) :
    ListAdapter<BaseItem, BaseViewHolder>(
        AsyncDifferConfig.Builder<BaseItem>(object : DiffUtil.ItemCallback<BaseItem>() {
            override fun areItemsTheSame(oldItem: BaseItem, newItem: BaseItem): Boolean {
                return oldItem.uniqueId == newItem.uniqueId
            }

            override fun areContentsTheSame(oldItem: BaseItem, newItem: BaseItem): Boolean {
                return oldItem == newItem
            }
        }).build()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        // The layoutId is used as the viewType
        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return BaseViewHolder(itemView)
    }

    override fun getItemViewType(position: Int) = getItem(position).layoutId

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position).bind(holder, itemClickCallback)
    }


    data class HeaderItem(val letter: String) : BaseItem {

        override val layoutId = R.layout.notification_head

        override val uniqueId = letter

        override fun bind(holder: BaseViewHolder, itemClickCallback: ((BaseItem) -> Unit)?) {
            holder.head.text = letter
        }
    }

    data class FruitItem(
        val context: Context,
        val description: String,
        val notiId: String,
        val sideImage: Int,
        val photoURL: String
    ) :
        BaseItem {

        override val layoutId = R.layout.notification_item

        override val uniqueId = notiId


        override fun bind(holder: BaseViewHolder, itemClickCallback: ((BaseItem) -> Unit)?) {
            holder.containerView.setOnClickListener { itemClickCallback?.invoke(this) }
            holder.body_text.text = description
            holder.side_image.setImageResource(sideImage)
            if (photoURL != "") {
                holder.event_image.visibility = View.VISIBLE
                Picasso.with(context).load(photoURL).into(holder.event_image)
            }
        }
    }
}