package com.example.seoulf3.searchstock.searchstocksize

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.data.ItemCode
import com.example.seoulf3.data.Quantity
import com.google.firebase.database.ktx.getValue

class SearchStockSizeViewModel : ViewModel() {
    private var itemCategoryCode: String = ""
    private var itemName: String = ""
    private var itemSizeCode: String = ""

    private var itemSizeList = mutableListOf<String>()

    private var itemCodeList = mutableListOf<String>()

    private var sizeMapItemCode = mutableMapOf<String, String>()
    private var itemCodeMapQ = mutableMapOf<String, String>()
    private var itemSizeMapQ = mutableMapOf<String, String>()


    fun getItemSizeList() = this.itemSizeList

    fun getItemSizeMapQ() = this.itemSizeMapQ
    fun setItemName(itemName: String) {
        this.itemName = itemName
    }

    fun setSizeCode(sizeCode: String) {
        this.itemSizeCode = sizeCode
    }

    fun setItemCategoryCode(categoryCode: String) {
        this.itemCategoryCode = categoryCode
    }

    fun getDataFromDatabase(callBack: DataBaseCallBack) {
        sizeMapItemCode = mutableMapOf()
        itemCodeMapQ = mutableMapOf()
        itemSizeMapQ = mutableMapOf()
        itemSizeList = mutableListOf()
        itemCodeList = mutableListOf()

        MainViewModel.database.child(DatabaseEnum.STANDARD.standard).child(itemSizeCode)
            .child("size").get()
            .addOnSuccessListener {
                val sizeList = it.value.toString().split(",")
                for (size in sizeList) {
                    itemSizeList.add(size.trim())
                }
                getItemCode(callBack)
            }
    }

    fun getItemCode(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.ITEMCODE.standard)
            .child(itemCategoryCode)
            .get()
            .addOnSuccessListener {
                if (!it.hasChildren()) {
                    Log.e("searchStock11", "non children")
                    callBack.callBack()
                    return@addOnSuccessListener
                }
                Log.e("searchStock11", "has children")


                val itemList = it.children

                for (i in itemList) {
                    val item = i.getValue<ItemCode>()!!
                    Log.e("searchStock11", i.key.toString())

                    if (item.itemName == itemName) {
                        Log.e("searchStock11", item.itemName.toString() + itemName.toString())
                        sizeMapItemCode[item.itemSize!!] = i.key.toString()
                        itemCodeList.add(i.key.toString())
                    }
                }
                checkQuantity(callBack)
            }
    }

    fun checkQuantity(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard)
            .child(itemCategoryCode)
            .get()
            .addOnSuccessListener {
                if (!it.hasChildren()) {
                    Log.e("searchStock11", "non childrenQ")
                    callBack.callBack()
                }
                Log.e("searchStock11", "has childrenQ")

                val qList = it.children

                for (i in qList) {

                    if (itemCodeList.contains(i.key.toString())) {
                        Log.e("searchStock11", "has data")
                        val item = i.getValue<Quantity>()!!
                        val q = item.quantity.toString().toInt()
                        val reQ = item.releaseQuantity.toString().toInt()
                        val sum = q - reQ
                        Log.e("searchStock11", sum.toString())
                        itemCodeMapQ[i.key.toString()] = sum.toString()
                    }
                }
                makeListData(callBack)
            }
    }

    fun makeListData(callBack: DataBaseCallBack) {

        for (s in itemSizeList) {
            val itemCode = sizeMapItemCode[s].toString()
            val q = itemCodeMapQ[itemCode].toString()
            itemSizeMapQ[s] = q

        }
        callBack.callBack()
    }
}
