package com.example.seoulf3.input

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.OrderKoreanFirst
import com.example.seoulf3.data.ItemName
import com.example.seoulf3.data.Quantity
import com.google.firebase.database.ktx.getValue
import kotlin.random.Random

class InputItemListViewModel : ViewModel() {

    private var chooseItemName = ""
    private var chooseItemCode = ""
    private var chooseItemSize = ""
    private var chooseItemPosition = ""
    private var inputItemQuantity = ""
    private var recommendPosition = ""
    private var firstC = true

    fun insertItemData(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).child(chooseItemCode).get()
            .addOnSuccessListener {
                MainViewModel.database.child(DatabaseEnum.ITEMCODE.standard).child(chooseItemName).child(chooseItemSize).child("itemCode").setValue(chooseItemCode)
                if (it.value == null) {
                    //todo 첫 아이템
                    MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).child(chooseItemCode).setValue(Quantity(inputItemQuantity, "0"))
                } else {
                    val item = it.getValue<Quantity>()
                    val inputN = item!!.quantity!!.toInt()
                    val plusN = inputItemQuantity.toInt()
                    val total = inputN + plusN
                    item.quantity = total.toString()
                    MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).child(chooseItemCode).setValue(item)
                }

                MainViewModel.database.child(DatabaseEnum.POSITION.standard).child(chooseItemPosition).child(chooseItemCode).get()
                    .addOnSuccessListener {
                        if (it.value == null) {
                            //todo 해당 자리 첫 데이터
                            MainViewModel.database.child(DatabaseEnum.POSITION.standard).child(chooseItemPosition).child(chooseItemCode).child("quantity").setValue(inputItemQuantity)
                        } else {
                            //todo 해당 자리 업데이트
                            val inputN = it.child("quantity").value.toString().toInt()
                            val plusN = inputItemQuantity.toInt()
                            val total = inputN + plusN
                            MainViewModel.database.child(DatabaseEnum.POSITION.standard).child(chooseItemPosition).child(chooseItemCode).child("quantity").setValue(total.toString())

                        }
                    }
                callBack.callBack()
            }
    }
    fun getRecommendPosition() = this.recommendPosition
    fun setChooseItemName(itemName: String) {
        this.chooseItemName = itemName
    }

    fun setChooseItemCode(itemCode: String) {
        this.chooseItemCode = itemCode
    }

    fun getChoosePosition() = this.chooseItemPosition

    fun getChooseItemName() = this.chooseItemName

    fun getChooseItemSize() = this.chooseItemSize
    fun setChooseItemSize(itemSize: String) {
        this.chooseItemSize = itemSize
    }

    fun setChooseItemPosition(position: String) {
        this.chooseItemPosition = position
    }

    fun setInputItemQuantity(quantity: String) {
        this.inputItemQuantity = quantity
    }


    private var itemNameList = mutableListOf<String>()
    private var itemCategory = mutableListOf<ItemName>()


    fun getItemNameInfoByIndex(index: Int): ItemName {
        val name = itemNameList[index]

        for (i in itemCategory) {
            if (i.name == name) {
                return i
            }
        }
        return ItemName()
    }

    fun getItemNameList() = this.itemNameList

    fun getItemCategory() = this.itemCategory

    fun getDataFromDatabase(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.ItemName.standard).get()
            .addOnSuccessListener {
                for (category in it.children) {
                    val item = category.getValue<ItemName>()!!
                    itemNameList.add(item.name.toString())
                    itemCategory.add(item)
                }
                itemNameList.sortWith(Comparator(OrderKoreanFirst::compare))
                callBack.callBack()
            }
    }

    fun getRecommendPosition(callBack: InputItemNameListActivity.RecommendPosition) {
        MainViewModel.database.child(DatabaseEnum.ITEMCODE.standard)
            .child(chooseItemName)
            .child(chooseItemSize)
            .child("itemCode").get()
            .addOnSuccessListener {
                if (it.value != null) {
                    this.chooseItemCode = it.value.toString()
                    //todo 코드 데이터는 가지고 있음
                    this.firstC = false
                    MainViewModel.database.child(DatabaseEnum.QUANTITY.standard)
                        .child(it.value.toString()).get()
                        .addOnSuccessListener {
                            if (it.value == null) {
                                //todo 코드는 있지만 수량이 없음
                                callBack.callBack("")
                            } else {
                                //todo 코드와 수량이 둘다 있음
                                MainViewModel.database.child(DatabaseEnum.POSITION.standard).get()
                                    .addOnSuccessListener {
                                        var recommendPosition = ""
                                        var quantity = "0"
                                        val positionList = it.children

                                        for (i in positionList) {
                                            val position = i.key
                                            val itemList = i.children

                                            for (j in itemList) {
                                                if (this.chooseItemCode == j.key) {
                                                    val valueNS = j.child("quantity").value.toString()
                                                    var n: Int
                                                    n = if (valueNS.isNullOrEmpty()) {
                                                        0
                                                    } else {
                                                        valueNS.toInt()
                                                    }
                                                    if (quantity.toInt() >= n || quantity.toInt() == 0) {
                                                        quantity = n.toString()
                                                        recommendPosition = position.toString()
                                                    }
                                                }
                                            }
                                        }
                                        this.recommendPosition = recommendPosition
                                        callBack.callBack(recommendPosition)
                                    }
                            }
                        }
                } else {
                    //todo 데이터가 없을경우
                    this.firstC = true
                    Log.e("itemCodeInRe", "data is null")
                    val rN = Random.nextLong(999999999999999999)
                    val rN2 = Random.nextLong(99999)
                    val code = rN.toString() + rN2
                    this.chooseItemCode = code
                    Log.e("itemCodeInRe", this.chooseItemCode + "cc")
                    callBack.callBack("")
                }
            }
            .addOnFailureListener { callBack.callBack("") }
    }
}