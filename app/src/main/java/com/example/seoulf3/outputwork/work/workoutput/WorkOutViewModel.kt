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
    private var cN = "workOutViewModel1"
    private var index: Int = 0
    private var positionList = mutableListOf<String>()
    private var positionIndex = 0
    private var workItem = WorkItem(0,0)

    private var date: String = ""

    data class WorkItem(var outPutItemQ: Int, var workedOutputItemQ: Int)

    data class ItemCodeWithItemList(var itemCode: String, var item: OutPutItem)

    private var itemList = mutableListOf<ItemCodeWithItemList>()


    fun setDate(date: String) {
        this.date = date
    }

    fun getDate() = this.date

    fun getDataFromDatabase(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.INPUTINFO.toString()).child(date).get()
            .addOnSuccessListener {
                val list = it.children

                for (i in list) {
                    val item = i.getValue<OutPutItem>()!!
                    val itemCode = i.key.toString()
                    itemList.add(ItemCodeWithItemList(itemCode, item))
                }
            }
        callBack.callBack()
    }

    fun getItemPosition(callBack: DataBaseCallBack): String {
        this.positionList = mutableListOf()

        if (index == itemList.size) {
            return "F"
        } else {
            val itemL = itemList[index]
            val itemCode = itemL.itemCode
            val item = itemL.item

            MainViewModel.database.child(DatabaseEnum.POSITION.standard).get()
                .addOnSuccessListener {
                    val positionList = it.children

                    for (i in positionList) {
                        Log.e(cN, i.key.toString() + "position")
                        val position = i.key.toString()
                        val itemList = i.children

                        for(_item in itemList) {
                            val _itemCode = _item.key.toString()
                            Log.e(cN, _itemCode + "ItemCode")
                            if (_itemCode == itemCode) {
                                val thisPQ = _item.child("quantityInPosition").value.toString()
                                Log.e(cN, thisPQ + "positionQuantity")
                                val itemQ = item.itemReleaseQ.toString()

                                this.positionList.add(position)
                            }
                        }
                    }
                }
        }
    }

    fun onClickNext() {
        //todo 다음 작업 넘어가기
    }

    fun onClickError() {
        //todo 데이터 베이스 에러 입력하기
    }
}