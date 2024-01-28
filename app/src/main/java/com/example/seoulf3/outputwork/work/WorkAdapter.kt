package com.example.seoulf3.outputwork.work

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.example.seoulf3.FirstSpellCheck
import com.example.seoulf3.data.OutPutItem
import com.example.seoulf3.databinding.SearchStockNameListBinding
import com.example.seoulf3.databinding.StockSizeListBinding

class WorkAdapter : BaseExpandableListAdapter() {
    private var itemNameList = mutableListOf<String>()
    private var itemSizeList = mutableMapOf<String, MutableList<OutPutItem>>()
    override fun getGroupCount(): Int = itemNameList.size

    override fun getChildrenCount(p0: Int): Int {
        val sizeList = itemSizeList[itemNameList[p0]]

        return if (sizeList.isNullOrEmpty()) {
            0
        } else {
            sizeList.size
        }
    }

    override fun getGroup(p0: Int): String = itemNameList[p0]

    override fun getChild(p0: Int, p1: Int): Any {

        val sizeList = itemSizeList[itemNameList[p0]]

        return if (sizeList.isNullOrEmpty()) {
            OutPutItem()
        } else {
            sizeList[p1]
        }
    }

    override fun getGroupId(p0: Int): Long = p0.toLong()

    override fun getChildId(p0: Int, p1: Int): Long = p1.toLong()

    override fun hasStableIds(): Boolean = false



    override fun getGroupView(p0: Int, p1: Boolean, p2: View?, p3: ViewGroup?): View {
        val binding: SearchStockNameListBinding = if (p2 == null) {
            SearchStockNameListBinding.inflate(LayoutInflater.from(p3!!.context))
        } else {
            SearchStockNameListBinding.bind(p2)
        }


        val itemName = itemNameList[p0]
        val spell = FirstSpellCheck.firstSpellCheckAndReturn(itemName)
        binding.tvSpell.text = " " + spell + " "
        binding.tvItemName.text = " "+ itemName



        if (p0 == 0 || spell != FirstSpellCheck.firstSpellCheckAndReturn(itemNameList[p0 - 1])) {
            binding.laySpell.visibility = View.VISIBLE
        } else {
            binding.laySpell.visibility = View.GONE
        }
        return binding.root

    }

    override fun getChildView(p0: Int, p1: Int, p2: Boolean, p3: View?, p4: ViewGroup?): View {
        val binding: StockSizeListBinding = if (p3 == null) {
            StockSizeListBinding.inflate(LayoutInflater.from(p4!!.context))
        } else {
            StockSizeListBinding.bind(p3)
        }

        val sizeData = getChild(p0, p1) as OutPutItem
        val size = sizeData.itemSize.toString()
        val q = sizeData.itemReleaseQ.toString()

        binding.tvItemSize.text = "         " + size
        binding.tvQuantity.text = q

        return binding.root
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean = false


    fun setItemNameList(list: MutableList<String>) {
        this.itemNameList = list
    }

    fun setSizeList(list: MutableMap<String, MutableList<OutPutItem>>) {
        this.itemSizeList = list
    }

}