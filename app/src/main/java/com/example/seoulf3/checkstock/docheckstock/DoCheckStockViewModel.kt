package com.example.seoulf3.checkstock.docheckstock

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.data.Quantity
import com.google.firebase.database.ktx.getValue

class DoCheckStockViewModel : ViewModel() {

    private data class PositionData(var position: String, var quantityInPo: Int)

    private object ItemInfo {
        var itemName: String = ""
        var itemSize: String = ""
        var itemCategoryCode: String = ""
        var itemCode: String = ""
    }

    private var positionDataList = mutableListOf<PositionData>()
    private var nowData = 0 // 데이터 현 위치값
    private var maxData = 0 // 데이터 맥스값

    private var checkedQ = 0 // 현재까지의 체크된 수량
    private var qInData = 0 // 체크해야되는 수량


    fun finishWork() {

        MainViewModel.database.child(DatabaseEnum.ERROR.standard)
            .child(getItemCode())
            .removeValue()

        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard)
            .child(getItemCategory())
            .child(getItemCode())
            .get().addOnSuccessListener {
                val item = it.getValue<Quantity>()!!
                item.quantity = checkedQ.toString()

                if (item.releaseQuantity.toString().toInt() > checkedQ) {
                    item.releaseQuantity = checkedQ.toString()
                }

                if (checkedQ == 0) {
                    //todo 데이터 삭제
                    MainViewModel.database.child(DatabaseEnum.QUANTITY.standard)
                        .child(getItemCategory())
                        .child(getItemCode())
                        .removeValue()
                } else {
                    MainViewModel.database.child(DatabaseEnum.QUANTITY.standard)
                        .child(getItemCategory())
                        .child(getItemCode()).setValue(item)
                }
            }

        for (i in positionDataList) {

            if (i.quantityInPo == 0) {
                MainViewModel.database.child(DatabaseEnum.POSITION.standard)
                    .child(i.position)
                    .child(getItemCode()).removeValue()
            } else {
                MainViewModel.database.child(DatabaseEnum.POSITION.standard)
                    .child(i.position)
                    .child(getItemCode())
                    .child("quantityInPosition")
                    .setValue(i.quantityInPo)
            }

        }
    }

    fun inputData(q: String): Boolean {
        val nowItem = positionDataList[nowData]
        nowItem.quantityInPo = q.toInt()
        checkedQ += q.toInt()
        nowData++
        return nowData == maxData
    }

    fun getNowData() = this.nowData.toString()

    fun getMaxData() = this.maxData.toString()

    fun getNowPosition(): String {
        val item = positionDataList[nowData]
        return item.position
    }

    fun getDataFromDataBase(callBack: DataBaseCallBack) {
        getQuantityDataFromDatabase(object : DataBaseCallBack {
            override fun callBack() {
                MainViewModel.database.child(DatabaseEnum.POSITION.standard).get()
                    .addOnSuccessListener {
                        val poList = it.children

                        for (p in poList) {
                            val position = p.key.toString() // 해당 포지션

                            val dataInPo = p.children

                            for (i in dataInPo) {

                                val itemCode = i.key.toString()// itemCode

                                if (ItemInfo.itemCode == itemCode) {
                                    val q = i.child("quantityInPosition").getValue()
                                        .toString().toInt()
                                    val data = PositionData(position, q)
                                    positionDataList.add(data)
                                }
                            }
                        }
                        maxData = positionDataList.size
                        callBack.callBack()
                    }
            }
        })
    }

    fun getQuantityDataFromDatabase(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard)
            .child(getItemCategory())
            .child(getItemCode())
            .child("quantity")
            .get()
            .addOnSuccessListener {
                val q = it.getValue().toString().toInt()
                this.qInData = q
                callBack.callBack()
            }
    }

    fun setItemName(name: String) {
        ItemInfo.itemName = name
    }

    fun setItemSize(size: String) {
        ItemInfo.itemSize = size
    }

    fun setItemCategory(category: String) {
        ItemInfo.itemCategoryCode = category
    }

    fun setItemCode(code: String) {
        ItemInfo.itemCode = code
    }

    fun getItemName() = ItemInfo.itemName

    fun getItemSize() = ItemInfo.itemSize

    fun getItemCategory() = ItemInfo.itemCategoryCode

    fun getItemCode() = ItemInfo.itemCode

    fun getNowPositionQ(): String {
        val nowData = positionDataList[nowData]
        return nowData.quantityInPo.toString()
    }


}