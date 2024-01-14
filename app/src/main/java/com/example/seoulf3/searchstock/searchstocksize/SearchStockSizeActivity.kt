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
import com.example.seoulf3.searchstock.SearchStockListAdapter

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
            viewModel =
                ViewModelProvider(this@SearchStockSizeActivity)[SearchStockSizeViewModel::class.java]
        }

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@SearchStockSizeActivity)
        }

        dialog.show()
        val itemName = intent.getStringExtra("name").toString()
        val itemSizeCode = intent.getStringExtra("size").toString()
        val itemCategoryCode = intent.getStringExtra("categoryCode").toString()

        viewModel.setItemName(itemName)
        viewModel.setItemSizeCode(itemSizeCode)
        viewModel.setItemCategoryCode(itemCategoryCode)
        viewModel.getDataFromDatabase(object : DataBaseCallBack {
            override fun callBack() {
                dialog.dismiss()
                setView()
                setOnClick()
            }

        })
        setContentView(binding.root)
    }

    fun setOnClick() {
        binding.btnBack.setOnClickListener { finish() }
    }

    fun setView() {
        binding.tvItemName.text = viewModel.getItemName()

        if (binding.lv.adapter == null) {
            binding.lv.adapter = StockListSizeAdapter()
        }
        (binding.lv.adapter as StockListSizeAdapter).setData(viewModel.getSizeMapQuantity())
        (binding.lv.adapter as StockListSizeAdapter).notifyDataSetChanged()
    }
}