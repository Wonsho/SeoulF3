package com.example.seoulf3.outputwork.outputnondata.scanposition

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.data.ErrorData
import com.example.seoulf3.data.Quantity
import com.google.firebase.database.ktx.getValue

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


    interface CallBack {
        fun callBack()
    }

    fun insertError(callBack: ScanPositionActivity.CallBackResult) {
        val errorData = ErrorData(this.itemCode, this.itemCategory, this.itemName, this.itemSize)
        MainViewModel.database.child(DatabaseEnum.ERROR.standard)
            .child(this.itemCode).setValue(errorData)
            .addOnSuccessListener {
                itemPositionDataList.removeAt(nowCount)
                maxCount--

                if (maxCount == 0) {
                    callBack.callBack(ScanPositionActivity.Result.FINISH)
                } else {
                    if (maxCount == nowCount) {
                        nowCount = 0
                    } else {
                        nowCount++
                    }
                    callBack.callBack(ScanPositionActivity.Result.NEXT)
                }
            }
    }

    fun insertErrorWithData(q: String, callBackResult: ScanPositionActivity.CallBackResult) {
        val nowPositionData = itemPositionDataList[nowCount]
        val outputQ = q.toInt()
        this.outputtedQ += outputQ
        decPositionQ(q, nowPositionData.position, object : CallBack {
            override fun callBack() {
                decQuantityData(q, object : CallBack {
                    override fun callBack() {
                        insertError(callBackResult)
                    }
                })
            }
        })


    }

    fun insertData(q: String, callBackResult: ScanPositionActivity.CallBackResult) {
        //todo outputtedQ++ *
        // database.position -- *
        // database.quantity -- *
        // if(outputtedQ == maxQ) -> finish *
        // .
        // else ->
        // PositionData :: quantityInPosition --
        // if (quantityInPosition == 0) -> itemPositionDataList.removeAt(nowCount)
        // maxCount-- *
        // if (maxCount.size -> 0) -> finish
        // .
        // else NextData
        val nowPositionData = itemPositionDataList[nowCount]
        val outputQ = q.toInt()
        this.outputtedQ += outputQ
        decPositionQ(q, nowPositionData.position, object : CallBack {
            override fun callBack() {
                decQuantityData(q, object : CallBack {
                    override fun callBack() {
                        //todo 다음 작업
                        if (outputQ.toInt() == maxQ.toInt()) {
                            callBackResult.callBack(ScanPositionActivity.Result.FINISH)
                        } else {
                            val qInP = nowPositionData.quantityInPosition
                            val decQ = qInP.toInt() - q.toInt()
                            nowPositionData.quantityInPosition = decQ

                            if (decQ == 0) {
                                itemPositionDataList.removeAt(nowCount)
                                maxCount--

                                if (maxCount == 0) {
                                    callBackResult.callBack(ScanPositionActivity.Result.FINISH)
                                    //todo return FINISH

                                } else {
                                    if (maxCount == nowCount) {
                                        nowCount = 0
                                    }
                                    callBackResult.callBack(ScanPositionActivity.Result.NEXT)
                                    //todo return NEXT
                                }
                            } else {
                                itemPositionDataList[nowCount] = nowPositionData
                                nowCount++
                                if (nowCount == maxCount) {
                                    nowCount = 0
                                }
                                callBackResult.callBack(ScanPositionActivity.Result.NEXT)
                                //todo return NEXT
                            }

                        }
                    }
                })
            }
        })
    }

    private fun decPositionQ(q: String, nowPosition: String, callBack: CallBack) {
        MainViewModel.database.child(DatabaseEnum.POSITION.standard)
            .child(nowPosition)
            .child(this.itemCode)
            .child("quantityInPosition").get()
            .addOnSuccessListener {

                val qInp = it.getValue().toString()
                val decResult = qInp.toInt() - q.toInt()

                if (decResult == 0) {
                    MainViewModel.database.child(DatabaseEnum.POSITION.standard)
                        .child(nowPosition)
                        .child(this.itemCode)
                        .child("quantityInPosition").removeValue()
                        .addOnSuccessListener {
                            callBack.callBack()
                        }
                } else {

                    MainViewModel.database.child(DatabaseEnum.POSITION.standard)
                        .child(nowPosition)
                        .child(this.itemCode)
                        .child("quantityInPosition").setValue(decResult.toString())
                        .addOnSuccessListener {
                            callBack.callBack()
                        }
                }

            }
    }

    fun decQuantityData(q: String, callBack: CallBack) {
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard)
            .child(this.itemCategory).child(this.itemCode)
            .get()
            .addOnSuccessListener {
                val data = it.getValue<Quantity>()!!
                val qInD = data.quantity.toString().toInt()
                val decResult = qInD - q.toInt()

                if (decResult == 0) {
                    MainViewModel.database.child(DatabaseEnum.QUANTITY.standard)
                        .child(this.itemCategory).child(this.itemCode).removeValue()
                        .addOnSuccessListener {
                            callBack.callBack()
                        }
                } else {
                    data.quantity = decResult.toString()
                    MainViewModel.database.child(DatabaseEnum.QUANTITY.standard)
                        .child(this.itemCategory).child(this.itemCode)
                        .setValue(data)
                        .addOnSuccessListener {
                            callBack.callBack()
                        }
                }
            }
    }


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