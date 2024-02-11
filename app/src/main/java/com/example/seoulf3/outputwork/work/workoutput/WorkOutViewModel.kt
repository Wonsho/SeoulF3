package com.example.seoulf3.outputwork.work.workoutput

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.AsyncListUtil.DataCallback
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.data.ErrorData
import com.example.seoulf3.data.OutPutItem
import com.google.firebase.database.core.view.DataEvent
import com.google.firebase.database.ktx.getValue

//todo 해당 리스트 아이템 가져옴
class WorkOutViewModel : ViewModel() {

    private var TAG = "WorkOutViewmodel!!"

    data class DataWithItemCode(
        var itemCode: String,
        var itemName: String,
        var itemReleaseQ: String,
        var itemSize: String,
        var itemCategoryCode: String
    )

    private var date: String = ""

    private object ItemCount {
        var maxCount = 0
        var nowCount = 0
    }

    private object SelectedItem {
        data class PositionData(var position: String, var quantity: Int)

        var releaseQ = 0
        var itemCode: String = ""
        var itemName: String = ""
        var itemSize: String = ""
        var itemCategoryCode: String = ""
        var itemPositionList = mutableListOf<PositionData>()
        var positionCount = 0
        var positionMaxPosition = 0
    }

    private var dataList = mutableListOf<DataWithItemCode>()

    fun getReleaseQ(): String {
        return SelectedItem.releaseQ.toString()
    }

    fun getSelectedItemName(): String {
        return SelectedItem.itemName
    }

    fun getSelectedItemSize(): String {
        return SelectedItem.itemSize
    }

    fun getSelectedItemPosition(): String {
        return getNowItemPosition()
    }

    fun getNowItemPositionQuantity(): String {
        val nowCount = SelectedItem.positionCount
        val positionData = SelectedItem.itemPositionList[nowCount]
        return positionData.quantity.toString()
    }


    fun getNowItemPosition(): String {
        val nowCount = SelectedItem.positionCount
        val positionData = SelectedItem.itemPositionList[nowCount]
        return positionData.position
    }

    fun setDate(date: String) {
        this.date = date
    }

    fun getItemSize(): String = ItemCount.maxCount.toString()
    fun getItemNowCount(): String = (ItemCount.nowCount + 1).toString()

    fun getOutputData(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.INPUTINFO.standard).child(this.date).get()
            .addOnSuccessListener {
                val dataList = it.children
                val mutableList = mutableListOf<DataWithItemCode>()

                for (data in dataList) {
                    val itemKey = data.key.toString()
                    val data = data.getValue<OutPutItem>()!!
                    Log.e(
                        TAG,
                        "$itemKey, ${data.itemName}, ${data.itemReleaseQ}, ${data.itemSize.toString()}"
                    )
                    mutableList.add(
                        DataWithItemCode(
                            itemKey,
                            data.itemName.toString(),
                            data.itemReleaseQ.toString(),
                            data.itemSize.toString(),
                            data.categoryCode.toString()
                        )
                    )
                }
                this.dataList = mutableList
                ItemCount.maxCount = this.dataList.size
                callBack.callBack()
            }
    }

    fun getDataAtPosition(callback: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.POSITION.standard).get()
            .addOnSuccessListener {
                val nowCount = ItemCount.nowCount
                val nowItem = dataList.get(nowCount)

                SelectedItem.releaseQ = nowItem.itemReleaseQ.toInt()
                SelectedItem.itemCode = nowItem.itemCode
                SelectedItem.itemName = nowItem.itemName
                SelectedItem.itemSize = nowItem.itemSize
                SelectedItem.itemCategoryCode = nowItem.itemCategoryCode
                SelectedItem.itemPositionList = mutableListOf()
                SelectedItem.positionCount = 0
                SelectedItem.positionMaxPosition = 0

                val positionData = it.children

                for (data in positionData) {
                    val position = data.key.toString()
                    Log.e(TAG, "position = $position")
                    val dataInPosition = data.children

                    for (item in dataInPosition) {
                        val itemCode = item.key.toString()
                        val q = item.child("quantityInPosition").value.toString()
                        if (itemCode == nowItem.itemCode) {
                            SelectedItem.itemPositionList.add(
                                SelectedItem.PositionData(
                                    position,
                                    q.toInt()
                                )
                            )
                        }
//                        Log.e(TAG, "itemCode = $itemCode")
//                        Log.e(TAG, "itemQ = $q")
                    }
                }

                val list = SelectedItem.itemPositionList

                SelectedItem.itemPositionList.sortBy(SelectedItem.PositionData::quantity)
                SelectedItem.positionCount = 0
                SelectedItem.positionMaxPosition = SelectedItem.itemPositionList.size
                for (i in list) {
                    Log.e(TAG, "itemCode!!! = ${i.position}")
                    Log.e(TAG, "itemQ!!! = ${i.quantity}")
                }
                callback.callBack()
            }
    }

//todo outputInfo data, Position Data, Quantity Data

    fun insertError(q: String) {
        val data = ErrorData(
            SelectedItem.itemCode,
            SelectedItem.itemCategoryCode,
            SelectedItem.itemName,
            SelectedItem.itemSize
        )
        MainViewModel.database.child(DatabaseEnum.ERROR.standard).child(SelectedItem.itemCode)
            .setValue(data)
        //todo 다음 포지션
    }

    fun checkData(q: String) {
        val nowData = dataList.get(ItemCount.nowCount)
        val releaseQ = nowData.itemReleaseQ.toInt()
        val inputQ = q.toInt()
        val decQ = releaseQ - inputQ
        nowData.itemReleaseQ = decQ.toString()

        dataList[ItemCount.nowCount] = nowData

        if (decQ == 0) {
            //todo 다음 아이템
            updateItemInfo("0")
        } else {
            //todo 다음 포지션
            updateItemInfo(decQ.toString())
        }
    }

    private fun updateItemInfo(q: String) {
        if (q == "0") {
            MainViewModel.database.child(DatabaseEnum.INPUTINFO.standard).child(this.date)
                .child(SelectedItem.itemCode).removeValue()
        } else {
            MainViewModel.database.child(DatabaseEnum.INPUTINFO.standard).child(this.date)
                .child(SelectedItem.itemCode).child("itemReleaseQ")
                .setValue(q)
        }
        updatePositionData(q)

    }

    fun updatePositionData(q: String) {
        if (q == "0") {
            //todo 현재 포지션 아이템 확인
        } else {

        }
    }

    fun updateQuantityData(q: String) {

    }
}