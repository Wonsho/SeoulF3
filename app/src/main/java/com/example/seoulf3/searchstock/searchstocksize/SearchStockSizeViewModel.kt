package com.example.seoulf3.searchstock.searchstocksize

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.data.Quantity
import com.google.firebase.database.ktx.getValue

class SearchStockSizeViewModel : ViewModel() {
    private var itemName: String = ""
    private var itemSizeCode: String = ""
    private var stockSizeList: MutableList<StockListSize> = mutableListOf()
    private var sizeList = mutableListOf<String>()
    private var sizeNameMap = mutableMapOf<String, Quantity>()

    interface CallbackInViewMode {
        fun callBack()
    }

    fun getStockSizeData(): MutableList<StockListSize> = this.stockSizeList

    fun setItemName(itemName: String) {
        this.itemName = itemName
    }

    fun setItemSizeCode(itemSizeCode: String) {
        this.itemSizeCode = itemSizeCode
    }

    fun getItemName(): String = this.itemName

    fun getItemSizeCode(): String = this.itemSizeCode

    fun getDataFromDB(callBack: DataBaseCallBack) {
        getSizeData(object : CallbackInViewMode {
            override fun callBack() {
                getQuantityData(callBack)
            }
        })
    }

    fun getQuantityData(callBack: DataBaseCallBack) {

        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).get()
            .addOnSuccessListener {
                val map = mutableMapOf<String, Quantity>()
                for (data in it.children) {
                    var item = data.getValue<Quantity>()!!

                    if (item.category == itemName) {
                        map[item.size!!] = item
                    }
                }
                this.sizeNameMap = map
                makeData()
                callBack.callBack()
            }
    }

    fun makeData() {
        for (size in sizeList) {
            val qC = sizeNameMap[size]

            if (qC == null) {
                this.stockSizeList.add(StockListSize(size, "0", "0"))
            } else {
                this.stockSizeList.add(StockListSize(size, qC.quantity!!, qC.releaseQ!!))
            }
        }
    }


    fun getSizeData(callbackInViewMode: CallbackInViewMode) {
        MainViewModel.database.child(DatabaseEnum.STANDARD.standard).child(this.itemSizeCode).get()
            .addOnSuccessListener {
                val a = it.child("size").value.toString()
                val sizeList = a.split(",")
                for (size in sizeList) {
                    this.sizeList.add(size.trim())
                }
                callbackInViewMode.callBack()
            }

    }

}