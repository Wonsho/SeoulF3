package com.example.seoulf3.outputupdate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.example.seoulf3.FirstSpellCheck
import com.example.seoulf3.data.Quantity
import com.example.seoulf3.databinding.InputItemUpdateChildListBinding
import com.example.seoulf3.databinding.SearchStockNameListBinding

class OutPutUpdateListAdapter(val callBack: OutPutUpdateActivity.CallBackDelete) : BaseExpandableListAdapter() {
    private var itemName = mutableListOf<String>()
    private var childData = mutableMapOf<String, MutableList<OutPutUpdateViewModel.Data>>()
    override fun getGroupCount(): Int = itemName.size.toInt()

    override fun getChildrenCount(p0: Int): Int = childData[itemName[p0]]!!.size

    override fun getGroup(p0: Int): String = itemName[p0]

    override fun getChild(p0: Int, p1: Int): Any = childData[itemName[p0]]!![p1]

    override fun getGroupId(p0: Int): Long = p0.toLong()

    override fun getChildId(p0: Int, p1: Int): Long = p1.toLong()

    override fun hasStableIds(): Boolean = true

    override fun getGroupView(p0: Int, p1: Boolean, p2: View?, p3: ViewGroup?): View {
        val binding = if (p2 == null) {
            SearchStockNameListBinding.inflate(LayoutInflater.from(p3!!.context))
        } else {
            SearchStockNameListBinding.bind(p2)
        }
        val name = itemName[p0]

        val spell = FirstSpellCheck.firstSpellCheckAndReturn(name)
        binding.tvSpell.text = " " + spell + " "
        binding.tvItemName.text = name
        if (p0 != 0 && FirstSpellCheck.firstSpellCheckAndReturn(itemName[p0 - 1]) == spell) {
            binding.laySpell.visibility = View.GONE
        } else {
            binding.laySpell.visibility = View.VISIBLE
        }
        return binding.root
    }

    override fun getChildView(p0: Int, p1: Int, p2: Boolean, p3: View?, p4: ViewGroup?): View {
        val binding = if (p3 == null) {
            InputItemUpdateChildListBinding.inflate(LayoutInflater.from(p4!!.context))
        } else {
            InputItemUpdateChildListBinding.bind(p3)
        }
        val item = getChild(p0,p1) as OutPutUpdateViewModel.Data
        binding.tvItemSize.text = item.item.size
        binding.tvReleaseQ.text = item.item.releaseQ
        callBack.callBack(p0,p1)
        return binding.root
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean = false

    fun setParents(itemNameList: MutableList<String>) {
        this.itemName = itemNameList
    }

    fun setChild(child: MutableMap<String, MutableList<OutPutUpdateViewModel.Data>>) {
        this.childData = child
    }

}