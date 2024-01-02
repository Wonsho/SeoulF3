package com.example.seoulf3.outputupdate

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.data.Category
import com.example.seoulf3.data.Quantity
import com.google.firebase.database.ktx.getValue

class OutPutUpdateViewModel : ViewModel() {
    data class KeyWithQuantity(var key: String, var quantity: Quantity)
    private var itemCategoryList = mutableListOf<Category>()
    private var itemNameList = mutableListOf<String>()
    private var mapByItem = mutableMapOf<String, KeyWithQuantity>()


    fun getItemCategory(callBack: DataBaseCallBack) {
        MainViewModel.database.child(DatabaseEnum.CATEGORY.standard).get()
            .addOnSuccessListener {
                for (item in it.children) {
                    val i = item.getValue<Category>()!!
                    itemCategoryList.add(i)
                    itemNameList.add(i.name!!)
                }
                callBack.callBack()
            }
    }

    fun getItemNameList() = this.itemNameList
}