package com.example.seoulf3.searchstock.searchstocksize

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.seoulf3.databinding.StockSizeListBinding

class StockListSizeAdapter : BaseAdapter() {
    private var sizeMapQuantity = mutableMapOf<String, String>()
    override fun getCount(): Int = sizeMapQuantity.size

    override fun getItem(p0: Int): String = ((sizeMapQuantity.keys) as MutableList<String>)[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val binding = if (p1 == null) {
            StockSizeListBinding.inflate(LayoutInflater.from(p2!!.context))
        } else {
            StockSizeListBinding.bind(p1)
        }

        val itemSize = getItem(p0)
        val itemQuantity = sizeMapQuantity[itemSize]!!

        binding.tvQuantity.text = itemQuantity
        binding.tvItemSize.text = itemSize

        return binding.root
    }

    fun setData(dataList: MutableMap<String, String>) {
        this.sizeMapQuantity = dataList
    }

}