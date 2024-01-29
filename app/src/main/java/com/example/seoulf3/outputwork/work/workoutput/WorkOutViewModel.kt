package com.example.seoulf3.outputwork.work.workoutput

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.data.OutPutItem
import com.google.firebase.database.ktx.getValue

//todo 해당 리스트 아이템 가져옴
class WorkOutViewModel : ViewModel() {

    val LOG = "WorkOutViewmodel!"

    data class DataWithItemCode(var itemCode: String, var data: OutPutItem)
    data class DataWorkCount(var times: Int, var size: Int)
    data class PositionCount(var times: Int, var size: Int)

    private var date = ""
    private var dataCount = DataWorkCount(0, 0)
    private var positionCount = PositionCount(0, 0)

    private var itemCodeMapPositionList = mutableMapOf<String, MutableList<String>>()
    private var itemCodeList = mutableListOf<String>()

    private var dataList = mutableListOf<DataWithItemCode>()

    fun getDataCount() = this.dataCount


    private fun getNowItemCode(): String =
        dataList.get(dataCount.times).itemCode

    fun setPositionData() {
        val nowItemCode = getNowItemCode()
        val positionList = itemCodeMapPositionList[nowItemCode]
        positionCount.size = positionList!!.size
        positionCount.times = 0
    }

    fun getNowPosition(): String {
        val nowItemCode = getNowItemCode()
        val positionList = itemCodeMapPositionList[nowItemCode]
        val nowPosition = positionList?.get(positionCount.times)
        return nowPosition.toString()
    }

    fun getDataByCount(count: Int): DataWithItemCode {
        return this.dataList[count]
    }

    fun setDataCount(dataCount: DataWorkCount) {
        this.dataCount = dataCount
    }

    fun getDate() = this.date

    fun setDate(date: String) {
        this.date = date
    }

    fun getDataFromDatabase(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.INPUTINFO.standard).child(this.date).get()
            .addOnSuccessListener {
                val list = it.children

                for (i in list) {
                    val item = i.getValue<OutPutItem>()!!
                    val itemCode = i.key.toString()
                    val data = DataWithItemCode(itemCode, item)
                    dataList.add(data)
                    if (!itemCodeList.contains(itemCode)) {
                        Log.e(LOG, "addedItemCode $itemCode")
                        itemCodeList.add(itemCode)
                    }
                }
                dataCount.size = dataList.size
                getItemPosition(callBack)
            }
    }

    private fun getItemPosition(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.POSITION.standard).get()
            .addOnSuccessListener {
                val positionItem = it.children

                for (i in positionItem) {
                    val position = i.key.toString()
                    Log.e(LOG, position + "this position")

                    val list = i.children

                    for (data in list) {
                        //todo item In position
                        Log.e(LOG, data.key.toString() + "this ItemCode")
                        val thisItemCode = data.key.toString()

                        if (itemCodeList.contains(thisItemCode)) {
                            Log.e(
                                LOG,
                                "this Item Position is $position, this Item Code is $thisItemCode"
                            )
                            val positionList = itemCodeMapPositionList[thisItemCode]

                            if (positionList.isNullOrEmpty()) {
                                //todo 첫 아이템
                                val newList = mutableListOf<String>()
                                newList.add(position)
                                itemCodeMapPositionList[thisItemCode] = newList
                            } else {
                                //todo 첫 아이템 아님
                                positionList.add(position)
                                itemCodeMapPositionList[thisItemCode] = positionList
                            }
                        }
                    }
                }
                callBack.callBack()
            }
    }
}