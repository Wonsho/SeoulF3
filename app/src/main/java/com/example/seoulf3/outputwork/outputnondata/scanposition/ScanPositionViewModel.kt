package com.example.seoulf3.outputwork.outputnondata.scanposition

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.outputwork.work.workoutput.WorkOutViewModel

class ScanPositionViewModel: ViewModel() {

    data class PositionData(var position: String, var quantityInPosition: Int)

    private var itemPositionDataList = mutableListOf<PositionData>()
    private var nowCount = 0
    private var maxCount = 0

    private var itemName = ""
    private var itemSize = ""
    private var itemCategory = ""
    private var itemCode = ""
    private var outputtedQ = 0
    private var maxQ = ""


    fun getNowPosition(): String {
        val nowItem = itemPositionDataList[nowCount]
        return nowItem.position
    }

    fun getOutputQ() = this.outputtedQ.toString()

    fun setOutputQ(outputQ: String) {
        val q = outputQ.toInt()
        outputtedQ += q
    }

    fun setItemName(name: String) {
        this.itemName = name
    }

    fun setItemSize(size: String) {
        this.itemSize = size
    }

    fun setItemCategory(category: String) {
        this.itemCategory = category
    }

    fun setItemCode(code: String) {
         this.itemCode = code
    }

    fun setMaxQ(maxQ: String) {
         this.maxQ = maxQ
    }

    fun getItemName() = this.itemName

    fun getItemSize() = this.itemSize

    fun getItemCategory() = this.itemCategory

    fun getItemCode() = this.itemCode

    fun getMaxQ() = this.maxQ


    fun getDataFromDatabase(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.POSITION.standard).get()
            .addOnSuccessListener {
                val positionList = it.children
                //todo 포지션 값

                for (p in positionList) {

                    val position = p.key.toString() // 포지션 값

                    val itemListInPosition = p.children

                    for (i in itemListInPosition) {

                        val itemCode = i.key.toString()// 아이템 코드

                        if (itemCode == this.itemCode) {
                            val q = i.child("quantityInPosition").value.toString()
                            val data = PositionData(position, q.toInt())
                            itemPositionDataList.add(data)
                        }
                    }
                    //todo 포지션 아이템 값
                }
                maxCount = itemPositionDataList.size
                itemPositionDataList.sortBy(PositionData::quantityInPosition)
                callBack.callBack()
            }
    }


}