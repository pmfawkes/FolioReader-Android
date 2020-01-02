package com.folioreader.ui.folio.adapter

import androidx.recyclerview.widget.RecyclerView

interface OnItemClickListener {
    fun onItemClick(adapter: androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>,
                    viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int, id: Long)
}