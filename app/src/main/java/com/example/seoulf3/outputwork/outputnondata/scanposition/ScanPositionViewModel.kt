package com.example.seoulf3.outputwork.outputnondata.scanposition

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.data.Position
import com.example.seoulf3.data.Quantity
import com.example.seoulf3.outputwork.work.workoutput.WorkOutViewModel
import com.google.firebase.database.ktx.getValue
import java.util.Objects

class ScanPositionViewModel : ViewModel() {

    data class PositionData(var position: String, var quantityInPosition: Int)

    private var itemPositionDataList = mutableListOf<PositionData>() // 포지션에대한 수량 데이터
    private var nowCount = 0 // 현재의 포지션 카운트
    private var maxCount = 0 // 총 포지션 카운트

    private var itemName = "" // 아이템이름
    private var itemSize = "" // 아이템 사이즈
    private var itemCategory = "" // 아이템 카테고리 코드
    private var itemCode = "" // 아이템 코드
    private var outputtedQ = 0 // 아이템 출고된 수량
    private var maxQ = "" // 아이템 출고해야되는 양

    fun getOutputQ() = this.outputtedQ.toString()

    fun getNowPositionQ(): String {
        val item = itemPositionDataList[nowCount]
        return item.quantityInPosition.toString()
    }
    fun getNowPosition(): String {
        val nowItem = itemPositionDataList[nowCount]
        return nowItem.position
    }


    fun setItemName(name: String) {
        this.itemName = name
    }

    fun setItemSize(size: String) {
        this.itemSize = size
    }

    fun setCategory(category: String) {
        this.itemCategory = category
    }

    fun setItemCode(code: String) {
        this.itemCode = code
    }

    fun setMaxQ(maxQ: String) {
        this.maxQ = maxQ
    }

    fun getMaxQ() = this.maxQ

    fun getItemName() = this.itemName

    fun getItemSize() = this.itemSize

    fun getItemCategory() = this.itemCategory

    fun getItemCode() = this.itemCode


    fun getDataFromDatabase(callBack: DataBaseCallBack) {

        if (itemPositionDataList.size != 0) {
            return
        }

        MainViewModel.database.child(DatabaseEnum.POSITION.standard).get()
            .addOnSuccessListener {
                val positionList = it.children

                for (position in positionList) {
                    val positionValue = position.key.toString()

                    val itemQ = position.child(itemCode).child("quantityInPosition").value

                    if (itemQ != null) {
                        //todo 해당 아이템 있음
                        val q = itemQ.toString()
                        val data = PositionData(positionValue, q.toInt())
                        itemPositionDataList.add(data)
                    }
                }
                maxCount = itemPositionDataList.size
                itemPositionDataList.sortBy(PositionData::quantityInPosition)
                callBack.callBack()
            }
    }
}