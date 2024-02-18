package com.example.seoulf3.outputwork.outputnondata.outputsizelist

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.data.ItemCode
import com.example.seoulf3.data.Quantity
import com.google.firebase.database.ktx.getValue

class OutPutWorkNonDataSizeListViewModel : ViewModel() {


    private var itemSizeMapQuantity = mutableMapOf<String, Quantity>()
    private var itemSizeMapItemCode = mutableMapOf<String, String>()
    private var itemCodeMapItemSize = mutableMapOf<String, String>()
    private var itemSizeList = mutableListOf<String>()


    fun getSizeByIndex(i: Int): String {
        return itemSizeList[i]
    }

    fun checkQuantityByIndex(i: Int): String {
        val qData = itemSizeMapQuantity[getSizeByIndex(i)]

        return if (qData == null) {
            "0"
        } else {
            val q = qData.quantity.toString().toInt()
            val rQ = qData.releaseQuantity.toString().toInt()

            val result = q - rQ
            result.toString()
        }
    }

    fun getItemCodeByIndex(i: Int): String {
        val itemCode = itemSizeMapItemCode[getSizeByIndex(i)]

        return if (itemCode.isNullOrBlank()) {
            ""
        } else {
            itemCode.toString()
        }
    }

    fun getItemSizeMapQuantity() = this.itemSizeMapQuantity

    fun getItemSizeList() = this.itemSizeList


    private var itemName = ""
    private var itemSizeCode = ""
    private var itemCategoryCode = ""

    fun setItemName(itemName: String) {
        this.itemName = itemName
    }

    fun setItemSizeCode(sizeCode: String) {
        this.itemSizeCode = sizeCode
    }

    fun setItemCategoryCode(categoryCode: String) {
        this.itemCategoryCode = categoryCode
    }

    fun getItemName() = this.itemName

    fun getItemSizeCode() = this.itemSizeCode

    fun getItemCategoryCode() = this.itemCategoryCode

    fun getDataFromDataBase(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.STANDARD.standard).child(itemSizeCode)
            .child("size").get()
            .addOnSuccessListener {
                val string = it.getValue().toString()
                Log.e("MMM", itemSizeCode.toString())
                val list = string.split(",")

                for (size in list) {
                    itemSizeList.add(size.trim())
                }
                getItemSizeMapItemCode(callBack)
            }
    }

    private fun getItemSizeMapItemCode(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.ITEMCODE.standard).child(itemCategoryCode).get()
            .addOnSuccessListener {
                val list = it.children

                for (i in list) {
                    val item = i.getValue<ItemCode>()!!

                    if (item.itemName == itemName) {
                        itemSizeMapItemCode[item.itemSize.toString()] = i.key.toString()
                        itemCodeMapItemSize[i.key.toString()] = item.itemSize.toString()
                    }
                }
                getItemSizeMapQuantity(callBack)
            }
    }

    private fun getItemSizeMapQuantity(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).child(itemCategoryCode).get()
            .addOnSuccessListener {
                val itemList = it.children

                for (i in itemList) {
                    val itemCode = i.key.toString()

                    val size = itemCodeMapItemSize[itemCode]

                    if (!size.isNullOrBlank()) {
                        itemSizeMapQuantity[size] = i.getValue<Quantity>()!!
                    }
                }
                callBack.callBack()
            }
    }

}