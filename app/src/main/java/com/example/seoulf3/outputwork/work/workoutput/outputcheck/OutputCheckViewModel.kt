package com.example.seoulf3.outputwork.work.workoutput.outputcheck

import androidx.lifecycle.ViewModel

class OutputCheckViewModel : ViewModel(){
    private var position = ""
    private var itemName = ""
    private var itemSize = ""
    private var itemNeedQ = ""
    private var itemSavedQ = ""


    fun getPosition() = this.position

    fun getItemName() = this.itemName

    fun getItemSize() = this.itemSize

    fun getItemNeedQ() = this.itemNeedQ

    fun getItemSavedQ() = this.itemSavedQ
    fun setPosition(position: String) {
        this.position = position
    }

    fun setItemName(itemName: String) {
        this.itemName = itemName
    }

    fun setItemSize(itemSize: String) {
        this.itemSize = itemSize
    }

    fun setItemNeedQ(itemNeedQ: String) {
        this.itemNeedQ = itemNeedQ
    }

    fun setItemSavedQ(savedQ: String) {
        this.itemSavedQ = savedQ
    }


}
