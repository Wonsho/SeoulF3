package com.example.seoulf3.searchstock

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.OrderKoreanFirst
import com.example.seoulf3.data.ItemName
import com.google.firebase.database.ktx.getValue

class SearchStockViewModel : ViewModel() {
    private var itemNameList = mutableListOf<ItemName>()
    private var onlyItemNameList = mutableListOf<String>()
    private var itemNameMapKey = mutableMapOf<String, String>()

    fun getItemName(i: Int): String {
        return onlyItemNameList[i]
    }

    fun getOnlyItemNameList() = this.onlyItemNameList

    fun getItemCategoryCode(i: Int): String {
        val itemName = onlyItemNameList.get(i)
        return itemNameMapKey[itemName]!!
    }

    fun getItemSizeCode(i: Int): String {
        val itemName = onlyItemNameList.get(i)

        for (item in itemNameList) {
            if (itemName == item.name) {
                return item.sizeCode!!
            }
        }
        return ""
    }

    fun getItemDataFromDatabase(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.ItemName.standard).get()
            .addOnSuccessListener {
                itemNameList = mutableListOf()
                itemNameMapKey = mutableMapOf()
                onlyItemNameList = mutableListOf()

                val list = it.children

                for (i in list) {
                    val item = i.getValue<ItemName>()!!
                    itemNameList.add(item)
                    onlyItemNameList.add(item.name!!)
                    itemNameMapKey[item.name.toString()] = i.key.toString()
                }
                onlyItemNameList.sortWith(Comparator(OrderKoreanFirst::compare))
                callBack.callBack()
            }
    }
}