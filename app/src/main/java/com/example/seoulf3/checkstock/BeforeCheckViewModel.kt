package com.example.seoulf3.checkstock

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.OrderKoreanFirst
import com.example.seoulf3.data.ErrorData
import com.google.firebase.database.ktx.getValue

class BeforeCheckViewModel : ViewModel() {

    private var itemNameList = mutableListOf<String>()
    private var itemNameMapItemData = mutableMapOf<String, ErrorData>()


    fun getErrorDataByIndex(i: Int): ErrorData {
        val itemName = itemNameList[i]
        return itemNameMapItemData[itemName]!!
    }
    fun getItemName() = this.itemNameList

    fun getErrorDataFromDatabase(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.ERROR.standard)
            .get()
            .addOnSuccessListener {
                val errorDataList = it.children

                for (i in errorDataList) {
                    val data = i.getValue<ErrorData>()!!

                    val itemName = data.itemName.toString()
                    val itemSize = data.itemSize.toString()

                    val nameWithSize = itemName + "  " + itemSize
                    itemNameMapItemData[nameWithSize] = data
                    itemNameList.add(nameWithSize)
                }
                itemNameList.sortWith(Comparator(OrderKoreanFirst::compare))
                callBack.callBack()
            }
    }

}