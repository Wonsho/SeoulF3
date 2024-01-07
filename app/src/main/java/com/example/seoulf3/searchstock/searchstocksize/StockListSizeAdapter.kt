package com.example.seoulf3.searchstock.searchstocksize

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.seoulf3.data.Quantity
import com.example.seoulf3.databinding.StockSizeListBinding

class StockListSizeAdapter : BaseAdapter() {
    private var parents = mutableListOf<String>()
    private var child = mutableMapOf<String, Quantity>()
    override fun getCount(): Int = parents.size

    override fun getItem(p0: Int): String = parents[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val binding = if (p1 == null) {
             StockSizeListBinding.inflate(LayoutInflater.from(p2!!.context))
        } else {
            StockSizeListBinding.bind(p1)
        }
        val size = parents[p0]
        val quantity = child[size]
        var quantityInfo = "0"
        if (quantity != null) {
            var inputN = quantity.quantity.toString().trim().toInt()
            var outN = quantity.releaseQuantity.toString().trim().toInt()

            quantityInfo = (inputN - outN).toString()
        }

        binding.tvItemSize.text = size
        binding.tvQuantity.text = quantityInfo
        return binding.root
    }

    fun setQuantity(sizeQuantity: MutableMap<String, Quantity>) {
        this.child = sizeQuantity
    }

    fun setSizeList(sizeList: MutableList<String>) {
        this.parents = sizeList
    }
}