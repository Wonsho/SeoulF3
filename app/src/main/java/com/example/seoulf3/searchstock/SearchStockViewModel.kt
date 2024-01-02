package com.example.seoulf3.searchstock

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.OrderKoreanFirst
import com.example.seoulf3.data.Category
import com.google.firebase.database.ktx.getValue

class SearchStockViewModel : ViewModel() {
    private var itemCategoryList = mutableListOf<Category>()
    private var itemNameList = mutableListOf<String>()


    fun getCategoryDataFromDB(dataBaseCallBack: DataBaseCallBack) {
        if (itemCategoryList.size > 1) {
            return
        }
        MainViewModel.database.child(DatabaseEnum.CATEGORY.standard).get()
            .addOnSuccessListener {
                val items = it.children

                for (item in items) {
                    itemCategoryList.add(item.getValue<Category>()!!)
                }

                for (category in itemCategoryList) {
                    itemNameList.add(category.name!!)
                }
                itemNameList.sortWith(Comparator(OrderKoreanFirst::compare))
                dataBaseCallBack.callBack()
            }
    }

    fun getCategorySizeCode(index: Int): Category {
        val itemName = itemNameList[index]

        for (item in itemCategoryList) {
            if (item.name == itemName) {
                return Category(itemName, item.sizeCode)
            }
        }
        return Category()
    }

    fun getItemList(): MutableList<String> {
        return this.itemNameList
    }

}