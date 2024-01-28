package com.example.seoulf3.outputwork.work

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.OrderKoreanFirst
import com.example.seoulf3.data.OutPutItem
import com.google.firebase.database.ktx.getValue

class WorkViewModel : ViewModel() {
    private var itemNameList = mutableListOf<String>()
    private var itemSizeList = mutableMapOf<String, MutableList<OutPutItem>>()
    private var itemDate = ""

    fun setItemDate(date: String) {
        this.itemDate = date
    }

    fun getItemNameList() = this.itemNameList

    fun getItemSizeList() = this.itemSizeList

    fun getData(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.INPUTINFO.standard).child(itemDate).get()
            .addOnSuccessListener {
                val itemList = it.children

                for (i in itemList) {
                    val item = i.getValue<OutPutItem>()!!
                    val itemName = item.itemName.toString()

                    val sizeList = itemSizeList[itemName]

                    if (!itemNameList.contains(itemName)) {
                        itemNameList.add(itemName)
                    }

                    if (sizeList == null) {
                        //todo 첫 아이템
                        val list = mutableListOf<OutPutItem>()
                        list.add(item)
                        itemSizeList[itemName] = list
                    } else {
                        //TODO 첫 아이템 아님
                        sizeList!!.add(item)
                        itemSizeList[itemName] = sizeList
                    }
                }
                itemNameList.sortWith(OrderKoreanFirst::compare)
                callBack.callBack()
            }
    }
}