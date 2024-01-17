package com.example.seoulf3.input

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.OrderKoreanFirst
import com.example.seoulf3.data.ItemCode
import com.example.seoulf3.data.ItemName
import com.example.seoulf3.data.Position
import com.example.seoulf3.data.Quantity
import com.google.firebase.database.ktx.getValue
import kotlin.random.Random

class InputItemListViewModel : ViewModel() {
    data class ChooseItemInfo(
        var itemName: String = "",
        var itemCategoryCode: String = "",
        var itemSizeCode: String = "",
        var itemPosition: String = "",
        var itemSize: String = "",
        var itemQuantity: String = "",
        var itemRecommendPosition: String = "",
        var itemCode: String = "",
        var hasQ: Boolean = true,
        var hasCode: Boolean = false
    )

    private var chooseItemInfo = ChooseItemInfo()
    private var itemNameList = mutableListOf<String>()
    private var itemCategoryList = mutableListOf<ItemName>()
    private var itemNameMapCategoryCode = mutableMapOf<String, String>()
    fun getItemNameDataFromDatabase(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.ItemName.standard).get()
            .addOnSuccessListener {
                val itemNameList = it.children

                for (i in itemNameList) {
                    val item = i.getValue<ItemName>()
                    val itemName = item!!.name
                    itemCategoryList.add(item!!)
                    this.itemNameList.add(itemName!!)
                    this.itemNameMapCategoryCode[itemName!!] = i.key.toString()
                }
                this.itemNameList.sortWith(Comparator(OrderKoreanFirst::compare))
                callBack.callBack()
            }
    }

    fun setChooseItem(chooseItemInfo: ChooseItemInfo) {
        this.chooseItemInfo = chooseItemInfo
    }

    fun getChooseItem() = this.chooseItemInfo
    fun getItemSizeCodeByIndex(index: Int): String {
        val itemName = getItemNameByIndex(index)

        for (i in itemCategoryList) {
            if (i.name == itemName) {
                return i.sizeCode!!
            }
        }
        return ""
    }


    fun getItemCategoryCodeByIndex(index: Int): String {
        val itemName = getItemNameByIndex(index)
        val itemCategoryCode = itemNameMapCategoryCode[itemName]
        return itemCategoryCode.toString()
    }

    fun getItemNameByIndex(index: Int): String {
        return this.itemNameList[index]
    }


    fun getItemNameList() = this.itemNameList

    fun getRecommendPosition(callBack: InputItemNameListActivity.CallbackPosition) {
        MainViewModel.database.child(DatabaseEnum.ITEMCODE.standard)
            .child(chooseItemInfo.itemCategoryCode).get()
            .addOnSuccessListener {
                val itemList = it.children

                for (i in itemList) {
                    val item = i.getValue<ItemCode>()!!

                    if (item.itemName == chooseItemInfo.itemName && item.itemSize == chooseItemInfo.itemSize) {
                        //todo 아이템 코드가 있음 -> 수량 체크
                        chooseItemInfo.hasCode = true
                        val itemCode = i.key.toString()
                        chooseItemInfo.itemCode = itemCode
                        checkQuantity(callBack)
                        return@addOnSuccessListener
                    }
                }
                //todo 아이템 코드가 없음 -> 생성
                chooseItemInfo.hasCode = false
                makeItemCode(callBack)
                return@addOnSuccessListener
            }
    }

    private fun checkQuantity(callBack: InputItemNameListActivity.CallbackPosition) {
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard)
            .child(chooseItemInfo.itemCategoryCode)
            .child(chooseItemInfo.itemCode).get()
            .addOnSuccessListener {

                if (it.value == null) {
                    Log.e("inputViewModel", "quantity is null")
                    //todo 수량이 없음
                    chooseItemInfo.hasQ = false
                    callBack.callBack("")
                    return@addOnSuccessListener
                } else {
                    //todo 수량이 있음
                    chooseItemInfo.hasQ = true
                    Log.e("inputViewModel", "quantity is not null")
                    checkRecommendPosition(callBack)
                    return@addOnSuccessListener
                }
            }
    }

    private fun checkRecommendPosition(callBack: InputItemNameListActivity.CallbackPosition) {
        MainViewModel.database.child(DatabaseEnum.POSITION.standard).get()
            .addOnSuccessListener {
                var recommendPosition = ""
                var quantity: Int = 999999999
                val itemListInPosition = it.children

                for (itemList in itemListInPosition) {
                    Log.e("input1", itemList.key.toString() + "=1")

                    val _itemList = itemList.children

                    for (i in _itemList) {
                        if (i.key == chooseItemInfo.itemCode) {
                            Log.e("input1", i.key.toString() + "=2")
                            Log.e("input1", i.child("quantityInPosition").value.toString() + "=3")
                            val q = i.child("quantityInPosition").value.toString().toInt()
                            if (q <= quantity) {
                                recommendPosition = itemList.key.toString()
                                quantity = q
                            }
                            break
                        }
                    }
                }
                callBack.callBack(recommendPosition)
                chooseItemInfo.itemRecommendPosition = recommendPosition
                return@addOnSuccessListener
            }
    }

    private fun makeItemCode(callBack: InputItemNameListActivity.CallbackPosition) {
        val code1 = Random.nextLong(999999999999999999).toString()
        val code2 = Random.nextLong(9999999999999).toString()
        val rCode = code1 + code2
        chooseItemInfo.itemCode = rCode
        callBack.callBack("")
    }

    private fun insertDataAtQuantity() {
        val item = chooseItemInfo
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).child(item.itemCategoryCode)
            .child(item.itemCode).setValue(Quantity(item.itemQuantity, "0"))
    }

    private fun insertDataAtPosition() {
        val item = chooseItemInfo
        MainViewModel.database.child(DatabaseEnum.POSITION.standard)
            .child(item.itemPosition)
            .child(item.itemCode)
            .setValue(Position(item.itemQuantity))
    }

    private fun insertItemCode() {
        val item = chooseItemInfo
        MainViewModel.database.child(DatabaseEnum.ITEMCODE.standard)
            .child(item.itemCategoryCode)
            .child(item.itemCode)
            .setValue(ItemCode(item.itemName, item.itemSize))
    }

    private fun updateQuantityData(callBack: DataBaseCallBack) {
        val item = chooseItemInfo
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard)
            .child(item.itemCategoryCode)
            .child(item.itemCode).get()
            .addOnSuccessListener {
                val _item = it.getValue<Quantity>()!!
                val itemQ = _item.quantity.toString().trim().toInt()
                val inputQ = chooseItemInfo.itemQuantity.trim().toInt()
                val sum = itemQ + inputQ
                _item.quantity = sum.toString()
                MainViewModel.database.child(DatabaseEnum.QUANTITY.standard)
                    .child(item.itemCategoryCode)
                    .child(item.itemCode)
                    .setValue(_item)
                callBack.callBack()
            }
    }

    private fun updateDataPosition(callBack: DataBaseCallBack) {
        val item = chooseItemInfo
        MainViewModel.database.child(DatabaseEnum.POSITION.standard)
            .child(item.itemPosition)
            .child(item.itemCode)
            .get()
            .addOnSuccessListener {
                val itemQ = it.value.toString().toInt()
                val inputQ = item.itemQuantity.trim().toInt()
                val sum = itemQ + inputQ

                MainViewModel.database.child(DatabaseEnum.POSITION.standard)
                    .child(item.itemPosition)
                    .child(item.itemCode)
                    .setValue(sum.toString())
                callBack.callBack()
            }
    }

    fun checkItemInPositionBeforeInsert(callBack: DataBaseCallBack) {
        val item = chooseItemInfo

        MainViewModel.database.child(DatabaseEnum.POSITION.standard)
            .child(item.itemPosition).child(item.itemCode).get()
            .addOnSuccessListener {
                if (it.value == null) {
                    //todo 해당 자리 아이템 없음
                    insertDataAtPosition()
                    callBack.callBack()
                } else {
                    //todo 해당 자리 아이템 있음
                    updateDataPosition(callBack)
                }
            }
    }
    fun insertData(callBack: DataBaseCallBack) {
        val data = chooseItemInfo

        if (data.hasCode) {
            //todo 코드가 있음
            if (data.hasQ) {
                //todo 수량이 있음
                //todo 해당 자리 아이템 있음 or 없음
                checkItemInPositionBeforeInsert(callBack)
                updateQuantityData(callBack)
            } else {
                //todo 수량이 없음
                insertDataAtQuantity()
                insertDataAtPosition()
                callBack.callBack()
            }
        } else {
            //todo 코드가 없음 <첫 아이템>
            insertItemCode()
            insertDataAtQuantity()
            insertDataAtPosition()
            callBack.callBack()
        }
    }
}