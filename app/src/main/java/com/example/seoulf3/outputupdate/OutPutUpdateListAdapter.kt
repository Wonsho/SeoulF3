package com.example.seoulf3.outputupdate

import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.example.seoulf3.data.Quantity

class OutPutUpdateListAdapter : BaseExpandableListAdapter() {
    private var parentsItemList = mutableListOf<String>()
    private var childData = mutableMapOf<String, MutableList<OutPutUpdateViewModel.KeyWithQuantity>>()

    override fun getGroupCount(): Int = parentsItemList.size.toInt()

    override fun getChildrenCount(p0: Int): Int = childData[parentsItemList[p0]]!!.size

    override fun getGroup(p0: Int): String = parentsItemList[p0]

    override fun getChild(p0: Int, p1: Int): OutPutUpdateViewModel.KeyWithQuantity = childData[parentsItemList[p0]!!]?.get(p1)!!

    override fun getGroupId(p0: Int): Long = p0.toLong()

    override fun getChildId(p0: Int, p1: Int): Long = p1.toLong()

    override fun hasStableIds(): Boolean = true

    override fun getGroupView(p0: Int, p1: Boolean, p2: View?, p3: ViewGroup?): View {
        TODO("Not yet implemented")
    }

    override fun getChildView(p0: Int, p1: Int, p2: Boolean, p3: View?, p4: ViewGroup?): View {
        TODO("Not yet implemented")
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean = true

    fun setParentsData(parents: MutableList<String>) {
        this.parentsItemList = parents
    }

    fun setChildData(children: MutableMap<String, MutableList<OutPutUpdateViewModel.KeyWithQuantity>>) {
        this.childData = children
    }
}