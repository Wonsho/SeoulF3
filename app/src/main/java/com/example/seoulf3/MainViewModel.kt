package com.example.seoulf3

import androidx.lifecycle.ViewModel
import com.example.seoulf3.data.Quantity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainViewModel : ViewModel() {


    companion object {
        lateinit var database: DatabaseReference
    }

    fun buildDatabase() {
        database = Firebase.database.reference
    }


}