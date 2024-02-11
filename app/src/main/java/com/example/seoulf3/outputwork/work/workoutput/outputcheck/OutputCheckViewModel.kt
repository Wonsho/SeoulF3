package com.example.seoulf3.outputwork.work.workoutput.outputcheck

import androidx.lifecycle.ViewModel

class OutputCheckViewModel : ViewModel(){
    private var position = ""
    private var itemName = ""
    private var itemSize = ""
    private var remainedBox = ""
    private var maxQ = ""

    fun setPosition(position: String) {
        this.position = position
    }

    fun setItemName(itemName: String) {
        this.itemName = itemName
    }

    fun setItemSize(itemSize: String) {
        this.itemSize = itemSize
    }

    fun setRemainedBox(remainedBox: String) {
        this.remainedBox = remainedBox
    }

    fun setMaxQ(maxQ: String) {
        this.maxQ = maxQ
    }

    fun getPosition() = this.position

    fun getItemName() = this.itemName

    fun getItemSize() = this.itemSize

    fun getItemRemainedBox() = this.remainedBox

    fun getMaxQ() = this.maxQ

}
