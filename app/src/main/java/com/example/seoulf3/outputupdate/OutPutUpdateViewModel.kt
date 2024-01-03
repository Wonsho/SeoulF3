package com.example.seoulf3.outputupdate

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.OrderKoreanFirst
import com.example.seoulf3.data.Category
import com.example.seoulf3.data.Quantity
import com.example.seoulf3.input.InputItemListViewModel
import com.google.firebase.database.ktx.getValue

class OutPutUpdateViewModel : ViewModel() {
    data class Data(var key: String, var item: Quantity)

    private val itemName = mutableListOf<String>()
    private val item = mutableMapOf<String, MutableList<Data>>()
    private val quantityList = mutableListOf<Data>()
    private var date = ""

    fun getItemNameList() = this.itemName

    fun getChildList() = this.item

    fun setDate() {
        //todo 초반에 날짜 넣기
        this.date = ""
    }

    fun dataCheck(): Boolean {
        for (itemName in itemName) {
            if (!item[itemName].isNullOrEmpty()) {
                return true
            }
        }
        return false
    }
    fun getDataFromDB(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).get()
            .addOnSuccessListener {
                val list = it.children

                for (item in list) {
                    val qItem = item.getValue<Quantity>()!!
                    val key = item.key!!
                    quantityList.add(Data(key, qItem))
                }
                makeData()
                callBack.callBack()
            }
    }

    private fun makeData() {
        for (item in quantityList) {

            if (!itemName.contains(item.item.category)) {
                itemName.add(item.item.category!!)
            }
        }
        itemName.sortWith(Comparator(OrderKoreanFirst::compare))

        for (item in itemName) {
            this.item[item] = mutableListOf()
        }
    }

}