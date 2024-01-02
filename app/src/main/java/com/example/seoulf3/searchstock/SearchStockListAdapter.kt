package com.example.seoulf3.searchstock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.seoulf3.FirstSpellCheck
import com.example.seoulf3.databinding.SearchStockNameListBinding

class SearchStockListAdapter : BaseAdapter() {

    private var itemNameList = mutableListOf<String>()

    override fun getCount(): Int = this.itemNameList.size

    override fun getItem(p0: Int): String = this.itemNameList[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val binding = if (p1 == null) {
            SearchStockNameListBinding.inflate(LayoutInflater.from(p2!!.context))
        } else {
            SearchStockNameListBinding.bind(p1)
        }
        val item = itemNameList[p0]
        binding.tvItemName.text = item
        binding.tvSpell.text = FirstSpellCheck.firstSpellCheckAndReturn(item) + "  "

        if (p0 != 0 && FirstSpellCheck.firstSpellCheckAndReturn(item) == FirstSpellCheck.firstSpellCheckAndReturn(
                itemNameList[p0 - 1]
            )
        ) {
            binding.laySpell.visibility = View.GONE
        } else {
            binding.laySpell.visibility = View.VISIBLE
        }

        return binding.root
    }

    fun setItemListData(itemListData: MutableList<String>) {
        this.itemNameList = itemListData
    }
}