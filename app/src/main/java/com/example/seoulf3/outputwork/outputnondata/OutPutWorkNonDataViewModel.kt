package com.example.seoulf3.outputwork.outputnondata

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.OrderKoreanFirst
import com.example.seoulf3.data.ItemName
import com.example.seoulf3.data.Quantity
import com.google.firebase.database.ktx.getValue

class OutPutWorkNonDataViewModel : ViewModel() {
    private var itemNameList = mutableListOf<String>()
    private var categoryList = mutableListOf<ItemName>()
    private var itemNameMapCategoryCode = mutableMapOf<String, String>()


    fun getItemByIndex(i: Int) = itemNameList[i]

    fun getItemCategoryCodeByIndex(i: Int): String {
        val itemName = getItemByIndex(i)
        return itemNameMapCategoryCode[itemName].toString()
    }

    fun getItemSizeCodeByIndex(i: Int): String {
        val itemName = getItemByIndex(i)

        for (i in categoryList) {
            if (itemName == i.name) {
                return i.sizeCode.toString()
            }
        }
        return ""
    }

    fun getItemNameList() = this.itemNameList
    fun getDataFromDatabase(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.ItemName.standard).get()
            .addOnSuccessListener {
                val list = it.children

                for (data in list) {
                    val item = data.getValue<ItemName>()!!
                    val itemName = item.name.toString()
                    val categoryKey = data.key.toString()
                    itemNameList.add(itemName)
                    categoryList.add(item)
                    itemNameMapCategoryCode[itemName] = categoryKey
                }
                this.itemNameList.sortWith(Comparator(OrderKoreanFirst::compare))
                callBack.callBack()
            }
    }
}