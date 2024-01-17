package com.example.seoulf3.outputwork

import androidx.lifecycle.ViewModel
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel

class OutPutWorkViewModel : ViewModel() {
    private var outputDateList = mutableListOf<String>()

    fun getDateList() = this.outputDateList
    fun getDataFromDataBase(callBack: DataBaseCallBack) {
        outputDateList = mutableListOf()

        MainViewModel.database.child(DatabaseEnum.INPUTINFO.standard).get()
            .addOnSuccessListener {
                val inputList = it.children
                for (date in inputList) {
                    outputDateList.add(date.key.toString())
                }
                //todo 순서 순서대로 바꾸기
                callBack.callBack()
            }
    }
}