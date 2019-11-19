package com.folioreader.ui.folio.adapter

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.folioreader.R
import com.folioreader.model.locators.SearchItemType
import com.folioreader.model.locators.SearchLocator

class SearchAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder> {

    companion object {
        @JvmField
        val LOG_TAG: String? = SearchAdapter::class.java.simpleName
        const val DATA_BUNDLE = "DATA_BUNDLE"
    }

    private val context: Context
    private var listViewType: ListViewType
    var searchLocatorList: MutableList<SearchLocator>? = null
        private set
    var onItemClickListener: OnItemClickListener? = null

    constructor(context: Context, dataBundle: Bundle) : super() {
        Log.v(LOG_TAG, "-> constructor")

        this.context = context
        listViewType = ListViewType.fromString(dataBundle.getString("ListViewType"))
        searchLocatorList = dataBundle.getParcelableArrayList("DATA")
    }

    fun changeDataBundle(dataBundle: Bundle) {

        listViewType = ListViewType.fromString(dataBundle.getString(ListViewType.KEY))
        searchLocatorList = dataBundle.getParcelableArrayList("DATA")
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {

        return if (searchLocatorList == null || searchLocatorList?.size == 0) {
            1
        } else {
            searchLocatorList!!.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return listViewType.value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {

        val viewHolder: ViewHolder

        when (viewType) {

            ListViewType.INIT_VIEW.value -> {

                val itemView: View = LayoutInflater.from(context)
                        .inflate(R.layout.item_search_init, parent, false)
                viewHolder = InitViewHolder(itemView)
            }

            ListViewType.LOADING_VIEW.value -> {

                val itemView: View = LayoutInflater.from(context)
                        .inflate(R.layout.item_search_loading, parent, false)
                viewHolder = LoadingViewHolder(itemView)
            }

            ListViewType.NORMAL_VIEW.value -> {

                val itemView: View = LayoutInflater.from(context)
                        .inflate(R.layout.item_search_normal, parent, false)
                viewHolder = NormalViewHolder(itemView)
            }

            ListViewType.EMPTY_VIEW.value -> {

                val itemView: View = LayoutInflater.from(context)
                        .inflate(R.layout.item_search_empty, parent, false)
                viewHolder = EmptyViewHolder(itemView)
            }

            ListViewType.FAILURE_VIEW.value -> {

                val itemView: View = LayoutInflater.from(context)
                        .inflate(R.layout.item_search_failure, parent, false)
                viewHolder = FailureViewHolder(itemView)
            }

            else -> throw UnsupportedOperationException("Unknown viewType = $viewType")
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {

        val viewHolder = holder as ViewHolder

        when (listViewType) {

            ListViewType.INIT_VIEW -> {
            }

            ListViewType.LOADING_VIEW -> {
            }

            ListViewType.NORMAL_VIEW -> viewHolder.onBind(position)

            ListViewType.EMPTY_VIEW -> {
            }

            ListViewType.FAILURE_VIEW -> {
            }

            else -> {
            }
        }
    }

    inner class InitViewHolder(itemView: View) : ViewHolder(itemView) {
        init {
            listViewType = ListViewType.INIT_VIEW
        }
    }

    inner class LoadingViewHolder(itemView: View) : ViewHolder(itemView) {
        init {
            listViewType = ListViewType.LOADING_VIEW
        }
    }

    inner class NormalViewHolder(itemView: View) : ViewHolder(itemView), View.OnClickListener {

        val textViewCount: TextView = itemView.findViewById(R.id.textViewCount)
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewResult: TextView = itemView.findViewById(R.id.textViewResult)
        lateinit var searchLocator: SearchLocator

        init {
            listViewType = ListViewType.NORMAL_VIEW
        }

        override fun onBind(position: Int) {

            itemPosition = position
            searchLocator = searchLocatorList!![position]

            when (searchLocator.searchItemType) {

                SearchItemType.SEARCH_COUNT_ITEM -> {
                    val count: Int = searchLocator.primaryContents.toInt()
                    textViewCount.text = context.resources.getQuantityString(
                            R.plurals.numberOfSearchResults, count, count)
                    textViewCount.visibility = View.VISIBLE
                    textViewTitle.visibility = View.GONE
                    textViewResult.visibility = View.GONE

                    itemView.setOnClickListener(null)
                }

                SearchItemType.RESOURCE_TITLE_ITEM -> {
                    textViewTitle.text = searchLocator.primaryContents
                    textViewTitle.visibility = View.VISIBLE
                    textViewCount.visibility = View.GONE
                    textViewResult.visibility = View.GONE

                    itemView.setOnClickListener(null)
                }

                SearchItemType.SEARCH_RESULT_ITEM -> {

                    val spannableString = SpannableString(searchLocator.text?.before
                            + searchLocator.text?.hightlight
                            + searchLocator.text?.after)
                    val from = searchLocator.text?.before?.length ?: 0
                    val to = from + (searchLocator.text?.hightlight?.length ?: 0)
                    spannableString.setSpan(StyleSpan(Typeface.BOLD), from, to, 0)
                    spannableString.setSpan(UnderlineSpan(), from, to, 0)
                    textViewResult.text = spannableString

                    textViewResult.visibility = View.VISIBLE
                    textViewCount.visibility = View.GONE
                    textViewTitle.visibility = View.GONE

                    itemView.setOnClickListener(this)
                }

                else -> {
                }
            }
        }

        override fun onClick(v: View?) {
            onItemClickListener?.onItemClick(this@SearchAdapter, this,
                    itemPosition, itemId)
        }
    }

    inner class EmptyViewHolder(itemView: View) : ViewHolder(itemView) {
        init {
            listViewType = ListViewType.EMPTY_VIEW
        }
    }

    inner class FailureViewHolder(itemView: View) : ViewHolder(itemView) {
        init {
            listViewType = ListViewType.FAILURE_VIEW
        }
    }
}