package com.example.seoulf3.outputwork.outputnondata.scanposition

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.data.Quantity
import com.example.seoulf3.outputwork.work.workoutput.WorkOutViewModel
import com.google.firebase.database.ktx.getValue
import java.util.Objects

class ScanPositionViewModel : ViewModel() {

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

    fun addOutputtedQ(q: String) {
        val quantity = q.toInt()
        outputtedQ += quantity

    }

    fun replaceQuantityData(q: String) {
        val item = itemPositionDataList[nowCount]
        item.quantityInPosition -= q.toInt()
        itemPositionDataList[nowCount] = item
    }

    fun insertData(callBack: DataBaseCallBack, q: String) {
        replacePositionDataInDB(callBack, q)
    }

    private fun replacePositionDataInDB(callBack: DataBaseCallBack, q: String) {
        MainViewModel.database.child(DatabaseEnum.POSITION.standard)
            .child(getNowPosition()).child(itemCode).child("quantityInPosition").get()
            .addOnSuccessListener {
                val q2 = it.value.toString().toInt()
                val result = q2 - q.toInt()

                if (result == 0) {
                    //todo 해당 포지션 데이터 삭제
                    MainViewModel.database.child(DatabaseEnum.POSITION.standard)
                        .child(getNowPosition())
                        .child(itemCode).child("quantityInPosition").removeValue()
                        .addOnSuccessListener {
                            replaceQuantityDataInDB(callBack, q)
                        }
                } else {
                    //todo 해당 포지션 데이터 수정
                    MainViewModel.database.child(DatabaseEnum.POSITION.standard)
                        .child(getNowPosition())
                        .child(itemCode).child("quantityInPosition").setValue(result.toString())
                        .addOnSuccessListener {
                            replaceQuantityDataInDB(callBack, q)
                        }
                }
            }
    }

    private fun replaceQuantityDataInDB(callBack: DataBaseCallBack, q: String) {
        Log.e("rep!!!", "$itemCategory,  $itemCode")
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).child(itemCategory)
            .child(itemCode).get()
            .addOnSuccessListener {
                val itemData = it.getValue<Quantity>()!!

                val p2 = itemData.quantity!!.toInt()
                val result = p2 - q.toInt()

                if (result == 0) {
                    MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).child(itemCategory)
                        .child(itemCode).removeValue()
                        .addOnSuccessListener { callBack.callBack() }
                } else {
                    MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).child(itemCategory)
                        .child(itemCode)
                        .setValue(Quantity(result.toString(), itemData.releaseQuantity))
                        .addOnSuccessListener { callBack.callBack() }
                }
            }
        callBack.callBack()
    }

    fun getMaxCount(): String {
       return this.maxCount.toString()
    }

    fun nextPosition() {
        val nowItem = itemPositionDataList[nowCount]
        if (nowItem.quantityInPosition == 0) {
            itemPositionDataList.removeAt(nowCount)
            maxCount--
        } else {
            nowCount ++

            if (nowCount == maxCount) {
                nowCount = 0
            } else {
                nowCount ++
            }
        }
    }

    fun checkQuantity(): Boolean {
        return outputtedQ == maxQ.toInt()
    }

    fun insertError() {
        //todo 에러 메세지 입력
    }

    fun getQuantityInPosition(): String {
        val nowPosition = itemPositionDataList[nowCount]
        return nowPosition.quantityInPosition.toString()
    }

    fun getNeedQ(): String {
        val maxQ = maxQ.toInt()
        val needQ = maxQ - outputtedQ
        return needQ.toString()
    }

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