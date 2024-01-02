package com.example.seoulf3.input

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.OrderKoreanFirst
import com.example.seoulf3.data.Category
import com.example.seoulf3.data.Position
import com.example.seoulf3.data.Quantity
import com.google.firebase.database.ktx.getValue
import kotlin.random.Random

class InputItemListViewModel : ViewModel() {
    private var position = ""
    private var quantity = ""
    private var size = ""
    private var itemName = ""

    private var times = 3
    private var itemCategoryList = mutableListOf<Category>()
    private var itemNameList = mutableListOf<String>()
    private var quantityDataList = mutableListOf<QuantityDataWithKey>()

    private var index = 0
    private var positionItemMap = mutableMapOf<String, MutableMap<String, Position>>()
    private var positionList = mutableListOf<String>()


    data class QuantityDataWithKey(var key: String, var data: Quantity)

    fun getSize() = this.size
    fun getItemName() = this.itemName

    fun setPosition(position: String) {
        this.position = position
    }

    fun setItemName(name: String) {
        this.itemName = name
    }

    fun setSize(size: String) {
        this.size = size
    }

    fun setQuantity(quantity: String) {
        this.quantity = quantity
    }

    fun getPosition() = this.position

    fun getRecommendPosition(): String {
        //todo 파레트안 품목이 4개 이하일 경우는 박스 개수 상관 없음
        var itemKey = ""

        for (item in quantityDataList) {
            if (item.data.category == this.itemName && item.data.size == this.size) {
                itemKey = item.key
                break
            }
            if (quantityDataList.lastIndex.equals(item)) {
                return ""
            }
        }

        for (position in positionList) {
            val list = positionItemMap[position]?.get(itemKey)

            if (list != null && positionItemMap[position]!!.keys.size <= 4 && list.quantity!!.toInt() <= 6) {
                return position
            }
        }
        return ""
    }

    fun getCategoryDataFromDB(dataBaseCallBack: DataBaseCallBack) {
        if (itemCategoryList.size > 1) {
            return
        }
        MainViewModel.database.child(DatabaseEnum.CATEGORY.standard).get()
            .addOnSuccessListener {
                val items = it.children

                for (item in items) {
                    itemCategoryList.add(item.getValue<Category>()!!)
                }

                for (category in itemCategoryList) {
                    itemNameList.add(category.name!!)
                }
                itemNameList.sortWith(Comparator(OrderKoreanFirst::compare))
                dataBaseCallBack.callBack()
            }
            .addOnFailureListener {
                dataBaseCallBack.callBack()
            }
    }

    fun getCategorySizeCode(index: Int): Category {
        val itemName = itemNameList[index]

        for (item in itemCategoryList) {
            if (item.name == itemName) {
                this.itemName = itemName
                return Category(itemName, item.sizeCode)
            }
        }
        return Category()
    }

    fun getItemList(): MutableList<String> {
        return this.itemNameList
    }

    //todo 수량 데이터 가져오기
    fun getQuantityDataFromDB(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).get()
            .addOnSuccessListener {
                for (data in it.children) {
                    val quantityData = data.getValue<Quantity>()
                    val key = data.key!!
                    quantityDataList.add(QuantityDataWithKey(key, quantityData!!))
                }
                callBack.callBack()
            }
            .addOnFailureListener {
                callBack.callBack()
            }
    }

    //todo 포지션 데이터 가져오기
    fun getPositionDataFromDB(callBack: DataBaseCallBack) {

        MainViewModel.database.child(DatabaseEnum.POSITION.standard).get()
            .addOnSuccessListener {
                for (key in it.children) {
                    positionList.add(key.key.toString())
                }
                callBack.callBack()
            }
    }

    fun getItemListFromPosition(callBack: DataBaseCallBack) {
        if (positionList.isNullOrEmpty()) {
            callBack.callBack()
        }

        for (position in positionList) {
            MainViewModel.database.child(DatabaseEnum.POSITION.standard).child(position).get()
                .addOnSuccessListener {
                    var map = mutableMapOf<String, Position>()

                    for (data in it.children) {
                        val item = data.getValue<Position>()!!
                        val key = data.key.toString()
                        map[key] = item
                    }
                    positionItemMap[position] = map
                    index++
                    if (index == positionList.size) {
                        callBack.callBack()
                    }
                }
        }
    }

    fun updateDataQuantityData(key: String) {
        Log.e("aaaaa1", "updateDataQuantityData")
        var item = QuantityDataWithKey("", Quantity())
        var n = 0
        for (data in quantityDataList) {
            if (data.key == key) {
                item = data
                var oriN = item.data.quantity!!.toInt()
                var inputN = quantity.toInt()
                item.data.quantity = (oriN + inputN).toString()
                quantityDataList[n] = item
                break
            }
            n++
        }
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).child(item.key).setValue(item.data)
    }

    fun insertDataQuantityData(key: String) {
        Log.e("aaaaa1", "insertDataQuantityData")
        val data = Quantity(itemName, quantity, "0", size)
        quantityDataList.add(QuantityDataWithKey(key, data))
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).child(key).setValue(data)
    }

    fun updateDataPositionData(key: String) {
        Log.e("aaaaa1", "updateDataPositionData")
        val item = positionItemMap[this.position]?.get(key)!!

        val oriN = item.quantity!!.toInt()
        val inputN =  quantity.toInt()


        item.quantity = (oriN + inputN).toString()

        positionItemMap[this.position]?.set(key, item)
        MainViewModel.database.child(DatabaseEnum.POSITION.standard).child(position).child(key)
            .setValue(item)
    }

    fun insertDataPositionData(key: String) {
        Log.e("aaaaa1", "insertDataPositionData")
        if (!positionList.contains(position)) {
            //todo 그 자리에 아무 아이템도 없을 경우
            positionList.add(position)
            val map = mutableMapOf<String, Position>()
            val item = Position(itemName, quantity, size)
            map[key] = item
            positionItemMap[position] = map
            MainViewModel.database.child(DatabaseEnum.POSITION.standard).child(position).child(key)
                .setValue(item)

        } else {
            //todo 그자리가 처음이 아닐경우
            val map = positionItemMap[position]!!
            val item = Position(itemName, quantity, size)
            map.put(key, item)
            positionItemMap.put(position, map)
            MainViewModel.database.child(DatabaseEnum.POSITION.standard).child(position).child(key)
                .setValue(item)
        }

    }

    fun checkPositionSameData(key: String) {
        val item = positionItemMap[this.position]?.get(key)

        if (item == null) {
            insertDataPositionData(key)
        } else {
            updateDataPositionData(key)
        }
    }


    fun setItemAtDataBase() {
        var dataKey: String = ""

        for (data in quantityDataList) {
            if (data.data.category == this.itemName && data.data.size == this.size) {
                dataKey = data.key
                updateDataQuantityData(dataKey)
                checkPositionSameData(dataKey)
                //todo 기존 수량 데이터 수정
                break
            }
        }

        fun checkDataKey(key: String): Boolean {
            for (data in quantityDataList) {
                if (data.key == key) {
                    return false
                }
            }
            return true
        }
        if (dataKey.isBlank()) {
            //todo 첫 수량 데이터 삽입
            while (true) {
                val key = Random.nextLong(999999999999999999).toString()
                if (checkDataKey(key)) {
                    dataKey = key
                    break
                }
            }
            insertDataQuantityData(dataKey)
            checkPositionSameData(dataKey)
        }
    }
}