package com.adilashraf.quizapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adilashraf.quizapp.databinding.HistoryListLayoutBinding
import com.adilashraf.quizapp.model.HistoryCoin
import java.util.Date

class HistoryCoinAdapter(var earnCoinList: ArrayList<HistoryCoin>)
    : RecyclerView.Adapter<HistoryCoinAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(val binding: HistoryListLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder
    = HistoryViewHolder(HistoryListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = earnCoinList.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val pos = earnCoinList[position]
        holder.binding.dateAndTime.text = pos.date
        holder.binding.earnCoins.text = pos.coin
    }
}