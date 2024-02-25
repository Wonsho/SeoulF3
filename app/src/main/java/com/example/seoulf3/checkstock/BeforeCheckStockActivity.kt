package com.example.seoulf3.checkstock

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.R
import com.example.seoulf3.checkstock.`in`.StockBarcodeScanActivity
import com.example.seoulf3.databinding.ActivityBeforeCheckStockBinding

class BeforeCheckStockActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBeforeCheckStockBinding
    private lateinit var viewModel : BeforeCheckViewModel
    private lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::binding.isInitialized) {
            binding = ActivityBeforeCheckStockBinding.inflate(layoutInflater)
        }

        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProvider(this@BeforeCheckStockActivity)[BeforeCheckViewModel::class.java]
        }

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@BeforeCheckStockActivity)
        }
        setContentView(binding.root)
    }


}