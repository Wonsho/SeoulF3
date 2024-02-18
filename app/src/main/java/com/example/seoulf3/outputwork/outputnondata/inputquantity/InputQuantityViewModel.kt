package com.example.seoulf3.outputwork.outputnondata.inputquantity

import androidx.lifecycle.ViewModel

class InputQuantityViewModel: ViewModel() {
    private var itemName = ""
    private var itemSize = ""
    private var itemCategory = ""
    private var itemCode = ""
    private var maxQ = ""


    fun getItemName() = this.itemName

    fun getItemSize() = this.itemSize

    fun getItemCategory() = this.itemCategory

    fun getItemCode() = this.itemCode

    fun getMaxQ() = this.maxQ


    fun setItemName(name: String) {
        this.itemName = name
    }

    fun setItemSize(size: String) {
        this.itemSize = size
    }

    fun setCategory(category: String) {
        this.itemCategory = category
    }

    fun setItemCode(itemCode: String) {
        this.itemCode = itemCode
    }

    fun setMaxQ(maxQ: String) {
        this.maxQ = maxQ
    }

}