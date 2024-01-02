package com.example.seoulf3.checkstock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.R
import com.example.seoulf3.checkstock.`in`.StockBarcodeScanActivity
import com.example.seoulf3.databinding.ActivityBeforeCheckStockBinding

class BeforeCheckStockActivity : AppCompatActivity() {
    interface CallBack {
        fun callBack()
    }

    private lateinit var binding: ActivityBeforeCheckStockBinding
    private var quantity: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::binding.isInitialized) {
            binding = ActivityBeforeCheckStockBinding.inflate(layoutInflater)
        }

        setContentView(binding.root)

        setOnClick()
    }

    private fun setOnClick() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnStartCheck.setOnClickListener {
            startActivity(Intent(this@BeforeCheckStockActivity, StockBarcodeScanActivity::class.java))
        }

    }

    private fun getDataFromDB(callBack: CallBack) {
        MainViewModel.database.child(DatabaseEnum.QUANTITY.standard).get()
            .addOnSuccessListener {
                this.quantity = it.childrenCount.toString()
                callBack.callBack()
            }
    }


}