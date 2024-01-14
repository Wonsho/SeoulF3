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
    private var chooseItemName = ""
    private var chooseItemCategoryCode = ""
    private var chooseItemPosition = ""
    private var chooseItemSizeCode = ""
    private var chooseItemQuantity = ""
    private var chooseItemSize = ""
    private var chooseItemCode = ""
    private var recommendPosition = ""
    private var hasQuantity = false

    private var itemCategoryList = mutableListOf<ItemName>()
    private var itemNameList = mutableListOf<String>()
    private var itemNameMapItemCategoryCode = mutableMapOf<String, String>()


    fun setChooseQuantity(q: String) {
        this.chooseItemQuantity = q
    }


    fun setChoosePosition(position: String) {
        this.chooseItemPosition = position
    }

    fun getRecommendPosition() = this.recommendPosition
    fun setChooseItemSize(size: String) {
        this.chooseItemSize = size
    }

    fun getChooseSize() = this.chooseItemSize

    fun resetData() {
        chooseItemName = ""
        chooseItemCategoryCode = ""
        chooseItemPosition = ""
        chooseItemSizeCode = ""
        chooseItemQuantity = ""
        chooseItemSize = ""
        chooseItemCode = ""
        recommendPosition = ""
        hasQuantity = false
    }

    fun getChooseItemName() = this.chooseItemName

    fun getChooseItemSizeCode() = this.chooseItemSizeCode

    fun getChooseItemCategoryCode() = this.chooseItemCategoryCode
    fun setItemNameByIndex(i: Int) {
        chooseItemName = itemNameList[i]
    }

    fun setItemSizeCodeByIndex(i: Int) {
        val itemName = itemNameList[i]

        for (item in itemCategoryList) {
            if (item.name == itemName) {
                chooseItemSizeCode = item.sizeCode!!
                break
            }
        }
    }

    fun setItemCategoryCodeByIndex(i: Int) {
        val itemName = itemNameList[i]
        chooseItemCategoryCode = itemNameMapItemCategoryCode[itemName]!!
    }

    fun getItemNameList() = this.itemNameList


    fun getDateFromDatabase(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.ItemName.standard).get()
            .addOnSuccessListener {
                val itemList = it.children

                for (i in itemList) {
                    val item = i.getValue<ItemName>()!!
                    val itemCategoryCode = i.key.toString()
                    val itemName = item.name
                    itemCategoryList.add(item)
                    itemNameList.add(itemName!!)
                    itemNameMapItemCategoryCode[itemName] = itemCategoryCode
                }
                this.itemNameList.sortWith(Comparator(OrderKoreanFirst::compare))
                callBack.callBack()
            }
    }


    fun getRecommendPosition(callBack: InputItemNameListActivity.RecommendCallBack) {
        //todo 아이템 코드가 있는지 조회 -> 없으면 임의의 코드 만들어주기
        // -> 있으면 -> 코드 입력

        MainViewModel.database.child(DatabaseEnum.ITEMCODE.standard).child(chooseItemCategoryCode)
            .get()
            .addOnSuccessListener {
                val itemList = it.children

                for (i in itemList) {
                    val item = i.getValue<ItemCode>()

                    if (item!!.itemName == chooseItemName && item!!.itemSize == chooseItemSize) {
                        //todo 아이템 코드가 존재
                        val itemCode = i.key.toString()
                        this.chooseItemCode = itemCode
                        //todo 수량 확인
                        checkHasQuantity(callBack)
                        return@addOnSuccessListener
                    }
                }
                val code1 = Random.nextLong(999999999999999999)
                val code2 = Random.nextLong(999999999)
                val resultCode = code1.toString() + code2
                this.chooseItemCode = resultCode
                MainViewModel.database.child(DatabaseEnum.ITEMCODE.standard)
                    .child(chooseItemCategoryCode).child(chooseItemCode)
                    .setValue(ItemCode(chooseItemName, chooseItemSize))
                callBack.callBack("")
            }
    }

    fun checkHasQuantity(callBack: InputItemNameListActivity.RecommendCallBack) {
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).child(chooseItemCategoryCode)
            .child(chooseItemCode).get()
            .addOnSuccessListener {
                val itemQ = it.getValue<Quantity>()

                if (itemQ == null) {
                    //todo 수량 없음
                    Log.e("checkQ", "Y")
                    this.hasQuantity = false
                    callBack.callBack("")
                } else {
                    this.hasQuantity = true
                    Log.e("checkQ", "N")
                    //todo 수량 있음
                    //todo 어디 포지션인지 체크
                    checkRecommendPosition(callBack)
                }
            }
    }

    fun checkRecommendPosition(callBack: InputItemNameListActivity.RecommendCallBack) {
        MainViewModel.database.child(DatabaseEnum.POSITION.standard).get()
            .addOnSuccessListener {
                var recommendPosition = ""
                var quantity = 100000000
                val positionList = it.children

                for (i in positionList) {
                    val position = i.key.toString()
                    val itemList = i.children

                    for (j in itemList) {
                        val itemCode = j.key.toString()
                        val itemQ = j.value.toString()
                        if (itemCode == chooseItemCode) {
                            if (itemQ.toInt() <= quantity) {
                                quantity = itemQ.toInt()
                                recommendPosition = position
                            }
                        }
                    }
                }
                callBack.callBack(recommendPosition)
            }
    }

    interface DataCallBack {
        fun callBack()
    }

    interface PositionDataCallBack {
        fun callBack(b: Boolean)
    }

    fun checkPositionHasSameData(callBack: PositionDataCallBack) {
        //todo 여기서 체크
        MainViewModel.database.child(DatabaseEnum.POSITION.standard)
            .child(chooseItemPosition).child(chooseItemCode).get()
            .addOnSuccessListener {
                Log.e("insertData", it.toString())

                if (it.value == null || it.value.toString().isBlank()) {
                    //todo 아이쳄 없음
                    callBack.callBack(false)
                    Log.e("checkPosition", "f")
                } else {
                    //todo 아이템 있음
                    callBack.callBack(true)
                    Log.e("checkPosition", "t")
                }
            }
    }

    fun insertData(callBack: DataBaseCallBack) {
        if (hasQuantity) {
            updateQuantityData(object : DataCallBack {
                override fun callBack() {
                    Log.e("insertData", "1")
                    //todo 해당 자리 아잍템 체크
                    checkPositionHasSameData(object : PositionDataCallBack {
                        override fun callBack(b: Boolean) {
                            if (b) {
                                Log.e("insertData", "2")
                                //todo 해당 자리에 데이터 있음
                                updatePositionData(object : DataCallBack {
                                    override fun callBack() {
                                        Log.e("insertData", "3")
                                        callBack.callBack()
                                    }

                                })
                            } else {
                                //todo 해당 자리에 데이터 없음
                                Log.e("insertData", "4")
                                insertPositionData(object : DataCallBack {
                                    override fun callBack() {
                                        Log.e("insertData", "5")
                                        callBack.callBack()
                                    }

                                })
                            }
                        }

                    })
                }

            })
            //todo 수량이 존재
            //todo 해당 자리에 존재
            //todo 해당 자리에 존재 하지 않음
        } else {
            //todo 수량이 존재 하지 않음
            insertQuantityData(object : DataCallBack {
                override fun callBack() {
                    insertPositionData(object : DataCallBack {
                        override fun callBack() {
                            callBack.callBack()
                        }
                    })
                }
            })
        }
    }

    private fun insertQuantityData(dataCallBack: DataCallBack) {
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).child(chooseItemCategoryCode)
            .child(chooseItemCode).setValue(Quantity(chooseItemQuantity, "0"))
        dataCallBack.callBack()
    }

    private fun insertPositionData(dataCallBack: DataCallBack) {
        Log.e("insertData", chooseItemPosition + "s")
        Log.e("insertData", chooseItemCode + "s")
        Log.e("insertData", chooseItemQuantity + "s")
        MainViewModel.database.child(DatabaseEnum.POSITION.standard).child(chooseItemPosition)
            .child(chooseItemCode).setValue(chooseItemQuantity)
        dataCallBack.callBack()
    }

    private fun updateQuantityData(dataCallBack: DataCallBack) {
        dataCallBack.callBack()
    }

    private fun updatePositionData(dataCallBack: DataCallBack) {
        dataCallBack.callBack()
    }
}