package com.example.seoulf3.outputwork.work.workoutput

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.data.ErrorData
import com.example.seoulf3.data.OutPutItem
import com.example.seoulf3.data.Position
import com.example.seoulf3.outputwork.outputnondata.scanposition.ScanPositionActivity
import com.example.seoulf3.outputwork.outputnondata.scanposition.ScanPositionViewModel
import com.google.firebase.database.ktx.getValue

//todo 해당 리스트 아이템 가져옴
class WorkOutViewModel : ViewModel() {
    data class OutputDataWithItemCode(var itemCode: String, var data: OutPutItem)
    data class PositionWithQuantity(var position: String, var positionQ: Int)

    private var date = ""


    private var infoList = mutableListOf<OutputDataWithItemCode>()
    private var infoListNowCount = 0
    private var infoListMaxSize = 0


    private var OuttedQ = 0
    private var NeedQ = 0

    fun getNeedQ() = this.NeedQ.toString()

    fun getOuttedQ() = this.OuttedQ.toString()

    fun goToNextItem() {
        //todo 다음 아이템으로 넘어가기

//        private var OuttedQ = 0
//        private var NeedQ = 0
        // 초기화
    }

    fun getNowNeedItemQ(): String {
        val q = infoListMaxSize - infoListNowCount
        return q.toString()
    }

    fun getNowItemName(): String {
        val item = infoList[infoListNowCount]
        return item.data.itemName.toString()
    }

    fun getNowItemSize(): String {
        val item = infoList[infoListNowCount]
        return item.data.itemSize.toString()
    }

    private var nowPositionItemList = mutableListOf<PositionWithQuantity>()
    private var nowPositionItemCount = 0
    private var nowPositionItemListMaxCount = 0


    fun getNowPositionItemSavedQ(): String = nowPositionItemList[nowPositionItemCount].positionQ.toString()
    fun getNowItemPosition(): String {
        val nowItem = nowPositionItemList[nowPositionItemCount]
        return nowItem.position.toString()
    }
    fun getNowPositionItemCount() = nowPositionItemCount.toString()

    fun getPositionItemListMaxCount() = nowPositionItemListMaxCount

    fun setDate(date: String) {
        this.date = date
    }


    //todo 현 나가야 되는 아이템 가져오기
    fun getDataOutputItemDataFromDatabase(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.INPUTINFO.standard).child(this.date.toString())
            .get()
            .addOnSuccessListener {
                val infoList = it.children

                for (i in infoList) {
                    val item = i.getValue<OutPutItem>()!!
                    val itemCode = i.key.toString()

                    val itemData = OutputDataWithItemCode(itemCode, item)
                    this.infoList.add(itemData)
                }
                infoListMaxSize = this.infoList.size
                infoListNowCount = 0
                this.NeedQ = this.infoList[infoListNowCount].data.itemReleaseQ!!.toInt()
                this.OuttedQ = 0
                callBack.callBack()
            }
    }

    fun getPositionDataByItemCodeFromDatabase(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.POSITION.standard).get()
            .addOnSuccessListener {
                val poList = it.children

                for (position in poList) {
                    val positionData = position.children
                    val positionString = position.key.toString()

                    for (data in positionData) {

                        if (data.key.toString() == infoList[infoListNowCount].itemCode) {
                            //todo 해당 데이터 맞음
                            val item = data.getValue<Position>()!!
                            val _data = PositionWithQuantity(positionString, item.quantityInPosition.toString().toInt())
                            nowPositionItemList.add(_data)
                        }
                    }
                }
                nowPositionItemList.sortBy(PositionWithQuantity::positionQ)
                nowPositionItemCount = 0
                nowPositionItemListMaxCount = nowPositionItemList.size
                callBack.callBack()
            }
    }

    fun insertError(callBack: WorkOutActivity.ResultCallBack) {
        val nowItem = infoList[infoListNowCount]
        val itemCode = nowItem.itemCode
        val itemName = nowItem.data.itemName.toString()
        val itemSize = nowItem.data.itemSize.toString()
        val itemCategory = nowItem.data.categoryCode.toString()

        val errorData = ErrorData(itemCode, itemCategory, itemName, itemSize)

        MainViewModel.database.child(DatabaseEnum.ERROR.standard).child(itemCode).setValue(errorData).addOnSuccessListener {

        }
    }
}