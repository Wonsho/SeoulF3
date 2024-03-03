package com.example.seoulf3.outputwork.work.workoutput

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.data.OutPutItem
import com.google.firebase.database.ktx.getValue

//todo 해당 리스트 아이템 가져옴
class WorkOutViewModel : ViewModel() {
    data class OutputDataWithItemCode(var itemCode: String, var data: OutPutItem)
    data class PositionWithQuantity(var position: String, var positionQ: Int)

    // 완료되면 해당 데이터 삭제
     var outputDataInfoList = mutableListOf<OutputDataWithItemCode>()

    private var date: String = ""
    private var outputDataInfoSize = 0  // 총 출고 아이템

     object NowItem {
        lateinit var nowInfoItem: OutputDataWithItemCode

        var nowItemOriginReleaseQ = 0  //총 출고 수량
        fun getNowItemName(): String = nowInfoItem.data.itemName!!

        fun getNowItemSize(): String = nowInfoItem.data.itemSize!!

        fun getOriginReleaseQ() = this.nowInfoItem.data.itemReleaseQ.toString()


        var dataPositionList = mutableListOf<PositionWithQuantity>()

        var releaseQ1 = 0 // 출고된 수량
        var nowCount1 = 0 // 현 포지션 현 인덱스
        var maxQ1 = 0 //현 아이템 포지션 데이터 맥스

        fun getReleaseQ() = this.releaseQ1

        fun getNowCount() = this.nowCount1

        fun getMaxQ() = this.maxQ1


    }

    fun setDate(date: String) {
        this.date = date
    }


    // 현재 아이템 품목 카운트 얻기
    fun getItemInfoDataNowCount(): String {
        val maxSize = getItemInfoDataSize()
        val nowItemSize = this.outputDataInfoList.size
        val result = maxSize.toInt() - nowItemSize
        return result.toString()
    }

    fun getItemInfoDataSize(): String {
        return outputDataInfoSize.toString()
    }

    fun getDataFromDB(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.INPUTINFO.standard).child(this.date).get()
            .addOnSuccessListener {
                MainViewModel.database.child(DatabaseEnum.INPUTINFO.standard).child(this.date)
                    .get()
                    .addOnSuccessListener {
                        val itemList = it.children

                        for (i in itemList) {
                            val item = i.getValue<OutPutItem>()
                            val itemCode = i.key.toString()

                            val data = OutputDataWithItemCode(itemCode, item!!)
                            this.outputDataInfoList.add(data)
                        }
                        outputDataInfoSize = outputDataInfoList.size
                        callBack.callBack()
                    }
            }
    }

    fun getPositionDataFromDatabase(callBack: DataBaseCallBack) {
        NowItem.nowInfoItem = outputDataInfoList[0]

        MainViewModel.database.child(DatabaseEnum.POSITION.standard).get()
            .addOnSuccessListener {
                val positionList = it.children

                for (p in positionList) {
                    val position = p.key.toString()
                    val itemInPositionList = p.children

                    for (item in itemInPositionList) {
                        if (item.key.toString() == NowItem.nowInfoItem.itemCode) {
                            val data = PositionWithQuantity(
                                position,
                                item.child("quantityInPosition").value.toString().toInt()
                            )
                            NowItem.dataPositionList.add(data)
                        }
                    }
                }
                NowItem.dataPositionList.sortBy(PositionWithQuantity::positionQ)
                NowItem.maxQ1

                callBack.callBack()
            }
    }

}