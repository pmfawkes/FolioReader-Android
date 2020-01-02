package com.folioreader.ui.folio.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.View

open class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    var listViewType: ListViewType = ListViewType.UNKNOWN_VIEW
    var itemPosition: Int = -1

    open fun onBind(position: Int) {}
}