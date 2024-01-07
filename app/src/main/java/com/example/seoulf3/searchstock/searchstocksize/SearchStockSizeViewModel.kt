package com.example.seoulf3.searchstock.searchstocksize

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.data.Quantity
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.getValue
import kotlin.random.Random

class SearchStockSizeViewModel : ViewModel() {
    //todo itemName을 받아 그에 맞는 사이즈와 코드를 받는다
    // 코드를 가지고 quantity를 참조 해당 사이즈의 개수와 출고 개수를 받는다.
    // 해당 아이템의 사이즈 코드를 가지고 해당 아이템의 사이즈 코드를 가지고 있는다
    private var sizeCode: String = ""
    private var itemName: String = ""
    private var sizeList = mutableListOf<String>()
    private var sizeCodeMap = mutableMapOf<String, String>()
    private var codeKey = mutableListOf<String>()
    private var codeQuantity = mutableMapOf<String, Quantity>()
    private var sizeQuantity = mutableMapOf<String, Quantity>()
    private var size = 0
    fun setSizeCode(sizeCode: String) {
        this.sizeCode = sizeCode
    }

    fun setItemName(itemName: String) {
        this.itemName = itemName
    }

    fun getSizeList() = this.sizeList

    fun getQuantityData() = this.sizeQuantity

    fun getSizeCode() = this.sizeCode

    fun getIteName() = this.itemName

    fun getSizeListDataFromDatabase(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.STANDARD.standard).child(sizeCode).child("size")
            .get()
            .addOnSuccessListener {
                val nameString = it.value.toString()

                val nameList = nameString.split(",")

                for (name in nameList) {
                    this.sizeList.add(name.trim())
                }
                getItemCode(callBack)
            }
    }

    fun getItemCode(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.ITEMCODE.standard).child(itemName).get()
            .addOnSuccessListener {
                for (i in it.children) {
                    val key = i.key.toString()
                    val value = i.child("itemCode").value.toString()
                    Log.e("getQuantityDataFromDatabase", key.toString())
                    Log.e("getQuantityDataFromDatabase", value.toString())
                    sizeCodeMap[key] = value
                    codeKey.add(value)
                }
                getQuantityDataFromDatabase(callBack)
            }
    }

    fun getQuantityDataFromDatabase(callBack: DataBaseCallBack) {
        size = 0
        Log.e("getQuantityDataFromDatabase", "start")
        if (codeKey.size == 0) {
            callBack.callBack()
        }
        for (code in codeKey) {
            Log.e("getQuantityDataFromDatabase", "start1")

            MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).child(code).get()
                .addOnSuccessListener {
                    val item = it.getValue<Quantity>()
                    if (item != null) {
                        Log.e("makeListData", item.quantity.toString())
                        codeQuantity[code] = item
                    }
                    size++
                    if (size == codeKey.size) {
                        makeListData(callBack)
                    }
                }
        }
    }

    fun makeListData(callBack: DataBaseCallBack) {

        for (size in sizeList) {
            var code = sizeCodeMap[size]

            if (!code.isNullOrEmpty()) {
                Log.e("makeListData", code)
                val q = codeQuantity[code]
                if (q != null) {
                    Log.e("makeListData", q.quantity.toString())
                    sizeQuantity[size] = q
                }
            }
        }
        callBack.callBack()
    }
}
