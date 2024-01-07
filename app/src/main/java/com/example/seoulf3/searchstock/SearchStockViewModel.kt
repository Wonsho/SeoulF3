package com.example.seoulf3.searchstock

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.OrderKoreanFirst
import com.example.seoulf3.data.ItemName
import com.google.firebase.database.ktx.getValue

class SearchStockViewModel : ViewModel() {
    private var categoryList = mutableListOf<ItemName>()
    private var itemNameList = mutableListOf<String>()

    fun getSizeCodeByItemName(index: Int): String {
        val itemName = itemNameList[index]

        for (category in categoryList) {
            if (category.name == itemName) {
                return category.sizeCode.toString()
            }
        }
        return ""
    }


    fun getItemNameByIndex(index: Int) = itemNameList[index]

    fun getItemNameList() = this.itemNameList

    fun getDataFromDatabase(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.ItemName.standard).get()
            .addOnSuccessListener {
                for (item in it.children) {
                    val category = item.getValue<ItemName>()!!
                    categoryList.add(category)
                    itemNameList.add(category.name!!)
                }
                itemNameList.sortWith(Comparator(OrderKoreanFirst::compare))
                callBack.callBack()
            }
    }

}