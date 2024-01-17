package com.example.seoulf3.outputwork.outputnondata

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.seoulf3.FirstSpellCheck
import com.example.seoulf3.databinding.SearchStockNameListBinding

class OutputWorkNonDataAdapter : BaseAdapter() {
    private var itemList = mutableListOf<String>()

    override fun getCount(): Int = itemList.size

    override fun getItem(p0: Int): String = itemList[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val binding = if (p1 == null) {
            SearchStockNameListBinding.inflate(LayoutInflater.from(p2!!.context))
        } else {
            SearchStockNameListBinding.bind(p1)
        }
        val itemName = itemList[p0]
        val spell = FirstSpellCheck.firstSpellCheckAndReturn(itemName)

        if (p0 == 0 || spell != FirstSpellCheck.firstSpellCheckAndReturn(itemList[p0 - 1])) {
            binding.laySpell.visibility = View.VISIBLE
         } else {
            binding.laySpell.visibility = View.GONE
        }
        return binding.root
    }

    fun setData(itemList: MutableList<String>) {
        this.itemList = itemList
    }
}