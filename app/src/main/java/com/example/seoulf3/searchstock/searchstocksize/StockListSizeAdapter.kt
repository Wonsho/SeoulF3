package com.example.seoulf3.searchstock.searchstocksize

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class StockListSizeAdapter : BaseAdapter() {

    private var itemSizeList = mutableListOf<String>()
    private var itemSizeMapQ = mutableMapOf<String, String>()


    //todo 아이템 카테고리 코드로 해당 아이템 다 불러옴 -> 이름이 맞는것만 가져옴
    override fun getCount(): Int  = itemSizeList.size

    override fun getItem(p0: Int) = itemSizeList[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        TODO("Not yet implemented")
    }

    fun setParentsData(itemSizeList: MutableList<String>) {
        this.itemSizeList = itemSizeList
    }

    fun setChildData(itemSizeMapQ: MutableMap<String, String>) {
        this.itemSizeMapQ = itemSizeMapQ

    }






}