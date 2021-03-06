package com.sportseventmanagement.`interface`

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

class BaseViewHolder(override val containerView: View)
    : RecyclerView.ViewHolder(containerView), LayoutContainer