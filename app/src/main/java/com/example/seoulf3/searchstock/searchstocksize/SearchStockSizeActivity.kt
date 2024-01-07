package com.example.seoulf3.searchstock.searchstocksize

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.databinding.ActivitySearchStockBinding
import com.example.seoulf3.databinding.ActivitySearchStockSizeBinding

class SearchStockSizeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchStockSizeBinding
    private lateinit var viewModel: SearchStockSizeViewModel
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::binding.isInitialized) {
            binding = ActivitySearchStockSizeBinding.inflate(layoutInflater)
        }

        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProvider(this)[SearchStockSizeViewModel::class.java]
        }

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@SearchStockSizeActivity)
        }
        setContentView(binding.root)
        dialog.show()
        viewModel.setItemName(intent.getStringExtra("itemName").toString())
        viewModel.setSizeCode(intent.getStringExtra("sizeCode").toString())
        viewModel.getSizeListDataFromDatabase(object : DataBaseCallBack {
            override fun callBack() {
                setView()
                setOnClick()
                dialog.dismiss()
            }
        })
    }

    private fun setOnClick() {
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setView() {
        binding.tvItemName.text = viewModel.getIteName()
        if (binding.lv.adapter == null) {
            binding.lv.adapter = StockListSizeAdapter()
            (binding.lv.adapter as StockListSizeAdapter).setSizeList(viewModel.getSizeList())
            (binding.lv.adapter as StockListSizeAdapter).setQuantity(viewModel.getQuantityData())
            (binding.lv.adapter as StockListSizeAdapter).notifyDataSetChanged()
        }
        (binding.lv.adapter as StockListSizeAdapter).notifyDataSetChanged()
    }

}