package com.example.seoulf3.input.inputitemsize

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel

class InputItemSizeViewModel : ViewModel() {
    private var itemName: String = ""
    private var itemSizeCode: String = ""
    private var sizeList = mutableListOf<String>()

    fun setItemName(name: String) {
        this.itemName = name
    }

    fun setItemSizeCode(sizeCode: String) {
        this.itemSizeCode = sizeCode
    }

    fun getSizeList() = this.sizeList

    fun getItemName() = this.itemName

    fun getItemSizeCode() = this.itemSizeCode

    fun getSizeDataFromDB(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.STANDARD.standard).child(itemSizeCode).get()
            .addOnSuccessListener {
                val size = it.child("size").value.toString()
                val sizeList = size.split(",")

                for (size in sizeList) {
                    this.sizeList.add(size.trim())
                }
                callBack.callBack()
            }
    }

}
