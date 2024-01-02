package com.example.seoulf3.searchstock.searchstocksize

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.seoulf3.data.Quantity
import com.example.seoulf3.databinding.StockSizeListBinding

class StockListSizeAdapter : BaseAdapter() {
    private var dataList = mutableListOf<StockListSize>()

    override fun getCount(): Int = dataList.size

    override fun getItem(p0: Int): StockListSize = dataList[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val binding = if (p1 == null) {
            StockSizeListBinding.inflate(LayoutInflater.from(p2!!.context))
        } else {
            StockSizeListBinding.bind(p1)
        }

        val item = dataList[p0]
        binding.tvItemSize.text = item.size
        binding.tvQuantity.text = item.quantity

        return binding.root
    }

    fun setData(dataList: MutableList<StockListSize>) {
        this.dataList = dataList
    }
}