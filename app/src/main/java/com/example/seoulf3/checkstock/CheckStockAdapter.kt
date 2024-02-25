package com.example.seoulf3.checkstock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.seoulf3.FirstSpellCheck
import com.example.seoulf3.databinding.SearchStockNameListBinding

class CheckStockAdapter : BaseAdapter() {

    private var itemNameList = mutableListOf<String>()
    override fun getCount(): Int = itemNameList.size

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

    fun setListData(data: MutableList<String>) {
        this.itemNameList = data
    }
}