package com.example.seoulf3.input.inputitemsize

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel

class InputItemSizeViewModel : ViewModel() {
    private var itemName = ""
    private var itemSizeCode = ""
    private var itemSizeList = mutableListOf<String>()

    fun setItemName(itemName: String) {
        this.itemName = itemName
    }

    fun setItemCode(code: String) {
        this.itemSizeCode = code
    }

    fun getItemName() = this.itemName

    fun getItemSizeListFromDatabase(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.STANDARD.standard).child(itemSizeCode).child("size").get()
            .addOnSuccessListener {
                val sizeString = it.value.toString()
                val list = sizeString.split(",")
                for (size in list) {
                    itemSizeList.add(size.trim())
                }
                callBack.callBack()
            }
    }

    fun getItemNameList() = this.itemSizeList
}
