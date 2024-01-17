package com.example.seoulf3.searchstock

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.OrderKoreanFirst
import com.example.seoulf3.data.ItemName
import com.google.firebase.database.ktx.getValue

class SearchStockViewModel : ViewModel() {
    private var itemNameList = mutableListOf<String>()
    private var itemCategoryDataList = mutableListOf<ItemName>()
    private var itemNameMapCategoryCode = mutableMapOf<String, String>()


    fun getItemNameByIndex(i: Int): String {
        val itemName = itemNameList[i].toString()
        return itemName
    }

    fun getItemCategoryCodeByIndex(i : Int): String {
        val itemName = getItemNameByIndex(i)
        return itemNameMapCategoryCode[itemName].toString()
    }

    fun getItemSizeCode(i : Int): String {
        val itemName = getItemNameByIndex(i)

        for (i in itemCategoryDataList) {
            if (i.name == itemName) {
                return i.sizeCode.toString()
            }
        }
        return ""
    }

    fun getItemNameList() = this.itemNameList
    fun getDataFromDatabase(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.ItemName.standard).get()
            .addOnSuccessListener {
                val itemList = it.children

                for (i in itemList) {
                    val item = i.getValue<ItemName>()!!
                    itemNameList.add(item.name!!)
                    itemCategoryDataList.add(item)
                    itemNameMapCategoryCode[item.name.toString()] = i.key.toString()
                }
                itemNameList.sortWith(Comparator(OrderKoreanFirst::compare))
                callBack.callBack()
            }
    }
}