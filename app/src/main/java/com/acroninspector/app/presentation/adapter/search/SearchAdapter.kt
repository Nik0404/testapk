package com.acroninspector.app.presentation.adapter.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.R
import com.acroninspector.app.databinding.ItemSearchResultBinding
import com.acroninspector.app.domain.entity.local.display.DisplaySearchHistory

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private lateinit var clickListener: OnClickSearchHistoryListener

    interface OnClickSearchHistoryListener {
        fun onClickSearchHistory(position: Int)
    }

    fun setClickListener(listener: OnClickSearchHistoryListener) {
        clickListener = listener
    }

    inline fun setOnItemClickListener(crossinline onClickSearchHistory: (Int) -> Unit) {
        setClickListener(object : OnClickSearchHistoryListener {

            override fun onClickSearchHistory(position: Int) = onClickSearchHistory(position)
        })
    }

    private var searchHistoryList: List<DisplaySearchHistory> = ArrayList()

    fun setData(data: List<DisplaySearchHistory>) {
        val diffUtilCallback = SearchDiffUtil(searchHistoryList, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)

        searchHistoryList = data
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSearchResultBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_search_result, parent, false
        )
        val holder = ViewHolder(binding)

        binding.root.setOnClickListener {
            clickListener.onClickSearchHistory(holder.adapterPosition)
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchHistoryList[position])
    }

    override fun getItemCount(): Int {
        return searchHistoryList.size
    }

    class ViewHolder(private val binding: ItemSearchResultBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(searchHistoryEntry: DisplaySearchHistory) {
            binding.searchHistoryEntry = searchHistoryEntry
            binding.executePendingBindings()
        }
    }
}
