package com.example.seoulf3.outputwork.outputnondata.outputsizelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.seoulf3.data.Quantity
import com.example.seoulf3.databinding.StockSizeListBinding

class OutPutWorkNonDataSizeAdapter : BaseAdapter() {
    private var itemSizeList = mutableListOf<String>()
    private var itemSizeMapQ = mutableMapOf<String, Quantity>()

    override fun getCount(): Int = itemSizeList.size

    override fun getItem(p0: Int): String = itemSizeList[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val binding = if (p1 == null) {
            StockSizeListBinding.inflate(LayoutInflater.from(p2!!.context))
        } else {
            StockSizeListBinding.bind(p1)
        }

        val size = itemSizeList[p0]
        val qData = itemSizeMapQ[size]

        val q = qData?.quantity?.toString()?.toInt() ?: 0
        val rQ = qData?.releaseQuantity?.toString()?.toInt() ?: 0

        val result = (q - rQ).toString()

        binding.tvItemSize.text = size
        binding.tvQuantity.text = result

        return binding.root
    }

    fun setSizeList(list: MutableList<String>) {
        this.itemSizeList = list
    }

    fun setQuantityData(qData: MutableMap<String, Quantity>) {
        this.itemSizeMapQ = qData
    }
}