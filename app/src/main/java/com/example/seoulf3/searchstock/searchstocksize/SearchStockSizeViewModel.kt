package com.example.seoulf3.searchstock.searchstocksize

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.data.ItemCode
import com.example.seoulf3.data.Quantity
import com.google.firebase.database.ktx.getValue

class SearchStockSizeViewModel : ViewModel() {
    private var itemCategoryCode = ""
    private var itemSizeCode = ""
    private var itemName = ""

    private var sizeList = mutableListOf<String>()
    private var sizeMapQuantityData = mutableMapOf<String, String>()
    private var itemCodeMapSize = mutableMapOf<String, String>()


    fun getItemName() = this.itemName
    fun getSizeMapQuantity() = this.sizeMapQuantityData
    fun setItemCategoryCode(categoryCode: String) {
        this.itemCategoryCode = categoryCode
    }

    fun setItemSizeCode(sizeCode: String) {
        this.itemSizeCode = sizeCode
    }

    fun setItemName(itemName: String) {
        this.itemName = itemName
    }

    fun getDataFromDatabase(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.STANDARD.standard).child(itemSizeCode).get()
            .addOnSuccessListener {
                val itemSizeString = it.value.toString()

                val sizeSplitList = itemSizeString.split(",")
                val sizeList = mutableListOf<String>()

                for (size in sizeSplitList) {
                    sizeList.add(size.trim())
                }

                getSizeItemCodeMap(callBack)
            }
    }

    private fun getSizeItemCodeMap(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.ITEMCODE.standard).child(itemCategoryCode).get()
            .addOnSuccessListener {
                if (it.childrenCount == 0L) {
                    callBack.callBack()
                }

                val itemList = it.children

                for (i in itemList) {
                    val item = i.getValue<ItemCode>()!!
                    if (this.itemName == item.itemName) {
                        sizeList.contains(item.itemSize)
                        itemCodeMapSize[i.key.toString()] = item.itemSize.toString()
                    }
                }
                getSizeMapQuantity(callBack)
            }
    }

    private fun getSizeMapQuantity(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).child(this.itemCategoryCode)
            .get()
            .addOnSuccessListener {
                val quantityList = it.children
                val itemCodeList = itemCodeMapSize.keys

                for (i in quantityList) {
                    if (itemCodeList.contains(i.key)) {
                        val item = i.getValue<Quantity>()!!
                        val reQ = item.releaseQuantity
                        if (reQ.isNullOrBlank()) {
                            "0"
                        } else {
                            reQ
                        }
                        val qN = item.quantity

                        if (qN.isNullOrBlank()) {
                            "0"
                        } else {
                            qN
                        }
                        val showN = (qN!!.toInt() - reQ!!.toInt()).toString()

                        val size = itemCodeMapSize[i.key]!!
                        sizeMapQuantityData[size] = showN
                    }
                }
                callBack.callBack()
            }
    }

}
