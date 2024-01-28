package com.example.seoulf3.outputupdate

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.OrderKoreanFirst
import com.example.seoulf3.data.ItemCode
import com.example.seoulf3.data.ItemName
import com.example.seoulf3.data.OutPutItem
import com.example.seoulf3.data.Quantity
import com.google.firebase.database.ktx.getValue
import java.time.LocalDateTime

class OutPutUpdateViewModel() : ViewModel() {

    var checkData = false
    private var itemNameStringList = mutableListOf<String>()
    private var itemDataList = mutableListOf<ItemName>()
    private var itemNameMapCategoryCode = mutableMapOf<String, String>()
    private var itemNameMapItemSizeCode = mutableMapOf<String, String>()
    private var childMap = mutableMapOf<String, MutableList<OutPutUpdateActivity.ChildData>>()

    fun getSizeCodeByIndex(i: Int): String {
        val itemName = getItemNameByIndex(i)

        return itemNameMapItemSizeCode[itemName].toString()
    }

    fun getItemNameByIndex(i: Int): String {
        return itemNameStringList[i].toString()
    }

    fun getItemCategoryCodeByIndex(i: Int): String {
        val itemName = getItemNameByIndex(i).toString()
        return itemNameMapCategoryCode[itemName].toString()
    }

    fun getParentsData() = this.itemNameStringList

    fun getChildData() = this.childMap
    fun getDataFromDatabase(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.ItemName.standard)
            .get()
            .addOnSuccessListener {
                val list = it.children

                for (i in list) {
                    val item = i.getValue<ItemName>()!!
                    val itemName = item.name
                    val sizeCode = item.sizeCode
                    itemDataList.add(item)
                    itemNameStringList.add(itemName!!)
                    itemNameMapCategoryCode[itemName] = i.key.toString()
                    itemNameMapItemSizeCode[itemName] = sizeCode!!
                }
                itemNameStringList.sortWith(OrderKoreanFirst::compare)
                callBack.callBack()
            }
    }

    fun insertDataInModel(data: OutPutUpdateActivity.ChildData) {
        var list = childMap[data.itemName]

        if (list.isNullOrEmpty()) {
            list = mutableListOf()
            list.add(data)
            this.childMap[data.itemName!!] = list
        } else {
            //todo check same Item
            for ((n, i) in list.withIndex()) {
                if (i.itemName == data.itemName && i.itemSize == data.itemSize) {
                    list[n] = data
                    this.childMap[data.itemName!!] = list
                    return
                }
            }
            list.add(data)
            this.childMap[data.itemName!!] = list
        }
    }

    fun deleteData(p1: Int, p2: Int) {
        val itemName = getItemNameByIndex(p1)
        var list = childMap[itemName]!!
        list.removeAt(p2)
        childMap[itemName] = list
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertAllData() {
        var itemCodeMap = mutableMapOf<String, OutPutItem>()
        var map = mutableMapOf<String, ItemCode>()
        var keyList = childMap.keys

        for (i in keyList) {
            val list = childMap[i]

            for (j in list!!) {
                val itemCode = j.itemCode.toString()
                val itemName = j.itemName.toString()
                val itemSize = j.itemSize.toString()
                val q = j.itemQ.toString()
                val item = OutPutItem(itemName, itemSize, q)
                itemCodeMap[itemCode] = item
            }
        }
        //todo 저장 outputInfo -> Date -> itemCode -> (itemName, itemSize, ReleaseQ)
        val date = System.currentTimeMillis().toString()
        MainViewModel.database.child(DatabaseEnum.INPUTINFO.standard).child(date)
            .setValue(itemCodeMap)
            .addOnSuccessListener {
                updateQuantity(itemCodeMap)
            }
        //todo 수량 수정 <중요작업!!>
    }

    private fun updateQuantity(childData: MutableMap<String, OutPutItem>) {
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).get()
            .addOnSuccessListener {
                val list = it.children
                //todo list key
                for (data in list) {
                    val categoryKey = data.key.toString()
                    Log.e("QuantityData", categoryKey)

                    val itemList = data.children
                    for (i in itemList) {
                        val itemQData = i.getValue<Quantity>()!!
                        val key = i.key.toString()
                        var index = 0

                        for (item in childData) {
                            if (item.key == key) {
                                //todo 해당 아이템
                                val oriN = itemQData.releaseQuantity!!.toInt()
                                val inputQ = item.value.itemReleaseQ!!.toInt()
                                val result = oriN + inputQ

                                itemQData.releaseQuantity = result.toString()
                                MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).child(categoryKey).child(key).setValue(itemQData)
                                break
                            }
                        }
                        //todo 수량 데이터 가짐
                        Log.e("QuantityData", i.key.toString())
                        Log.e("QuantityData", i.value.toString())
                    }
                }
            }
    }
}