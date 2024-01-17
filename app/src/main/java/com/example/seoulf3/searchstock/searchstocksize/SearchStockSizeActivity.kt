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
        val sizeCode = intent.getStringExtra("size")
        val category = intent.getStringExtra("category")
        val itemName = intent.getStringExtra("name")

        viewModel.setSizeCode(sizeCode.toString())
        viewModel.setItemName(itemName.toString())
        viewModel.setItemCategoryCode(category.toString())
        setContentView(binding.root)
        dialog.show()
        viewModel.getDataFromDatabase(object : DataBaseCallBack {
            override fun callBack() {
                dialog.dismiss()
                setListView()
                setOnClick()
            }

        })
    }


    fun setListView() {
        if (binding.lv.adapter == null) {
            binding.lv.adapter = StockListSizeAdapter()
        }
        (binding.lv.adapter as StockListSizeAdapter).setParentsData(viewModel.getItemSizeList())
        (binding.lv.adapter as StockListSizeAdapter).setChildData(viewModel.getItemSizeMapQ())
        (binding.lv.adapter as StockListSizeAdapter).notifyDataSetChanged()

    }

    fun setOnClick() {
        binding.btnBack.setOnClickListener { finish() }
    }
}