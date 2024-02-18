package com.example.seoulf3.outputwork.work.workoutput

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.AsyncListUtil.DataCallback
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.data.ErrorData
import com.example.seoulf3.data.OutPutItem
import com.example.seoulf3.data.Quantity
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

    interface CallBack {
        fun callBack()
    }

    fun checkData(q: String, callBack: WorkOutActivity.CheckCallBack) {
        val nowData = dataList.get(ItemCount.nowCount)
        val releaseQ = nowData.itemReleaseQ.toInt()
        val inputQ = q.toInt()
        val decQ = releaseQ - inputQ
        nowData.itemReleaseQ = decQ.toString()

        dataList[ItemCount.nowCount] = nowData

        //todo 만약 포지션 카운트가 맥스랑 같은경우 에러를 띄우고 다음 아이템으로 넘어감

        if (decQ == 0) {
            //todo 다음 아이템
            updateItemInfo("0", inputQ.toString(), object : CallBack {
                override fun callBack() {
                    //todo 다음 아이템으로 이동
                    ItemCount.nowCount += 1
                    //todo 아이템 새로 고침
                    callBack.callBack(WorkOutActivity.Notion.NEXT_ITEM)
                }

            })
        } else {
            //todo 다음 포지션
            updateItemInfo(decQ.toString(), inputQ.toString(), object : CallBack {
                override fun callBack() {
                    //todo 다음 포지션으로 이동
                    SelectedItem.positionCount += 1
                    callBack.callBack(WorkOutActivity.Notion.NEXT_POSITION)
                }
            })
        }
    }

    private fun updateItemInfo(q: String, qInPosition: String, callBack: CallBack) {
        //q -> 현재 아이템이 나가야 하는 수량
        //qInPosition -> 현재 나간 수량

        if (q == "0") {
            MainViewModel.database.child(DatabaseEnum.INPUTINFO.standard).child(this.date)
                .child(SelectedItem.itemCode).removeValue()
        } else {
            MainViewModel.database.child(DatabaseEnum.INPUTINFO.standard).child(this.date)
                .child(SelectedItem.itemCode).child("itemReleaseQ")
                .setValue(q)
        }
        updatePositionData(q, qInPosition, callBack)

    }

    fun updatePositionData(q: String, qInPosition: String, callback: CallBack) {

        //q -> 현재 아이템이 나가야 하는 수량
        //qInPosition -> 현재 나간 수량
        val nowPositionData = SelectedItem.itemPositionList[SelectedItem.positionCount]
        val nowPosition = nowPositionData.position
        val itemQuantityInPosition = nowPositionData.quantity
        val stockQ = itemQuantityInPosition - qInPosition.toInt()
        nowPositionData.quantity = stockQ
        SelectedItem.itemPositionList[SelectedItem.positionCount] = nowPositionData
        val outPutQ = qInPosition.toInt()

        MainViewModel.database.child(DatabaseEnum.POSITION.standard).child(nowPosition)
            .child(SelectedItem.itemCode).child("quantityInPosition").get()
            .addOnSuccessListener {
                val qInPo = it.value.toString().toInt()

                if ((qInPo - outPutQ) == 0) {
                    MainViewModel.database.child(DatabaseEnum.POSITION.standard).child(nowPosition)
                        .child(SelectedItem.itemCode).removeValue()

                } else {
                    MainViewModel.database.child(DatabaseEnum.POSITION.standard).child(nowPosition)
                        .child(SelectedItem.itemCode).child("quantityInPosition")
                        .setValue(qInPo.toString())
                }
                updateQuantityData(q, qInPosition, callback)
            }

//        if (q == "0") {
//            //todo 현재 포지션 아이템 확인
//            //todo 에러가 없음
//            //todo 다음 아이템으로 진행
//        } else {
//            //todo 에러 체크
//        }
    }

    fun updateQuantityData(q: String, qInPosition: String, callback: CallBack) {
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard)
            .child(SelectedItem.itemCategoryCode).child(SelectedItem.itemCode).get()
            .addOnSuccessListener {
                val item = it.getValue<Quantity>()!!
                var q = item.quantity.toString().toInt() - qInPosition.toInt()
                var rQ = item.releaseQuantity.toString().toInt() - qInPosition.toInt()
                item.quantity = q.toString()
                item.releaseQuantity = rQ.toString()

                if (q == 0) {
                    MainViewModel.database.child(DatabaseEnum.QUANTITY.standard)
                        .child(SelectedItem.itemCategoryCode).child(SelectedItem.itemCode)
                        .removeValue()
                    callback.callBack()
                } else {
                    MainViewModel.database.child(DatabaseEnum.QUANTITY.standard)
                        .child(SelectedItem.itemCategoryCode).child(SelectedItem.itemCode)
                        .setValue(item)
                    callback.callBack()
                }

            }
    }
}