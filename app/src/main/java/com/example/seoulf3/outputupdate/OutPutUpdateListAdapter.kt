package com.example.seoulf3.outputupdate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.example.seoulf3.FirstSpellCheck
import com.example.seoulf3.databinding.InputItemUpdateChildListBinding
import com.example.seoulf3.databinding.SearchStockNameListBinding
import kotlinx.coroutines.flow.callbackFlow

class OutPutUpdateListAdapter(val callBack: OutPutUpdateActivity.CallBackDelete) :
    BaseExpandableListAdapter() {

    private var parentsData = mutableListOf<String>()
    private var childData = mutableMapOf<String, MutableList<OutPutUpdateActivity.ChildData>>()

    override fun getGroupCount(): Int = parentsData.size

    override fun getChildrenCount(p0: Int): Int {
        val data = childData[parentsData[p0]]
        return if (data.isNullOrEmpty()) {
            0
        } else {
            data.size
        }
    }

    override fun getGroup(p0: Int): String = parentsData[p0]

    override fun getChild(p0: Int, p1: Int): OutPutUpdateActivity.ChildData {
        val list = childData[parentsData[p0]]?.get(p1)

        return list ?: OutPutUpdateActivity.ChildData()
    }

    override fun getGroupId(p0: Int): Long = p0.toLong()
    override fun getChildId(p0: Int, p1: Int): Long = p1.toLong()

    override fun hasStableIds(): Boolean = false

    override fun getGroupView(p0: Int, p1: Boolean, p2: View?, p3: ViewGroup?): View {
        val binding = if (p2 == null) {
            SearchStockNameListBinding
                .inflate(LayoutInflater.from(p3!!.context))
        } else {
            SearchStockNameListBinding.bind(p2)
        }
        val itemName = parentsData[p0]
        val firstSpell = FirstSpellCheck.firstSpellCheckAndReturn(itemName)
        binding.tvItemName.text = itemName
        binding.tvSpell.text = " " + firstSpell + " "

        if (p0 == 0 || firstSpell != FirstSpellCheck.firstSpellCheckAndReturn(parentsData[p0 - 1])) {
            binding.laySpell.visibility = View.VISIBLE
        } else {
            binding.laySpell.visibility = View.GONE
        }
        return binding.root
    }

    override fun getChildView(p0: Int, p1: Int, p2: Boolean, p3: View?, p4: ViewGroup?): View {
        val binding = if (p3 == null) {
            InputItemUpdateChildListBinding.inflate(LayoutInflater.from(p4!!.context))
        } else {
            InputItemUpdateChildListBinding.bind(p3)
        }
        val child = getChild(p0, p1)
        binding.tvItemSize.text = child.itemSize
        binding.tvReleaseQ.text = child.itemQ
        binding.btnDelete.setOnClickListener {
            callBack.callBack(p0, p1)
        }
        return binding.root
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return false
    }

    fun setParentsData(parentsData: MutableList<String>) {
        this.parentsData = parentsData
    }

    fun setChildData(childData: MutableMap<String, MutableList<OutPutUpdateActivity.ChildData>>) {
        this.childData = childData
    }
}