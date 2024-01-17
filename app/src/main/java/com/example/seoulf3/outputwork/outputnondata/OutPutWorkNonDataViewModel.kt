package com.example.seoulf3.outputwork.outputnondata

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.data.ItemName
import com.example.seoulf3.data.Quantity
import com.google.firebase.database.ktx.getValue

class OutPutWorkNonDataViewModel : ViewModel() {
    private var itemList = mutableListOf<String>()
    private var category = mutableListOf<ItemName>()
    private var codeQuantityMap = mutableMapOf<String, Quantity>()

    fun getDataFromDataBase(callBack: OutPutWorkNonDataActivity.CallBackInOutPut) {
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).get()
            .addOnSuccessListener {
                codeQuantityMap = mutableMapOf()
                itemList = mutableListOf()
                category = mutableListOf()

                if (it.childrenCount == 0L) {
                    callBack.callBack(true)
                }

                val stockList = it.children

                for (data in stockList) {
                    val _data = data.getValue<Quantity>()!!
                    codeQuantityMap[data.key!!] = _data
                }

            }
    }
}