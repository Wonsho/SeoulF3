package com.example.seoulf3.searchstock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.seoulf3.FirstSpellCheck
import com.example.seoulf3.data.ItemName
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

        val itemName = itemNameList[p0]
        val firstSpell = FirstSpellCheck.firstSpellCheckAndReturn(itemName)
        binding.tvSpell.text = " "+firstSpell+" "
        binding.tvItemName.text = itemName

        if (p0 == 0 || firstSpell != FirstSpellCheck.firstSpellCheckAndReturn(itemNameList[p0 - 1])) {
            binding.laySpell.visibility = View.VISIBLE
        } else {
            binding.laySpell.visibility = View.GONE
        }
        return binding.root
    }

    fun setData(itemNameList: MutableList<String>) {
        this.itemNameList = itemNameList
    }


}