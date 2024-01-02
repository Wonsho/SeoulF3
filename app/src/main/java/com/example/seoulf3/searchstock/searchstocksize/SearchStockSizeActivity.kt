package com.example.seoulf3.searchstock.searchstocksize

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.databinding.ActivitySearchStockSizeBinding

class SearchStockSizeActivity : AppCompatActivity() {
    private lateinit var viewModel: SearchStockSizeViewModel
    private lateinit var binding: ActivitySearchStockSizeBinding
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProvider(this)[SearchStockSizeViewModel::class.java]
        }

        if (!::binding.isInitialized) {
            binding = ActivitySearchStockSizeBinding.inflate(layoutInflater)
        }

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@SearchStockSizeActivity)
        }
        setContentView(binding.root)
        dialog.show()
        getIntentData()
        setView()

    }

    private fun getIntentData() {
        val itemName = if (intent.getStringExtra("name").isNullOrEmpty()) {
            ""
        } else {
            intent.getStringExtra("name")!!
        }

        val itemSizeCode = if (intent.getStringExtra("size").isNullOrEmpty()) {
            ""
        } else {
            intent.getStringExtra("size")!!
        }

        viewModel.setItemSizeCode(itemSizeCode)
        viewModel.setItemName(itemName)
        viewModel.getDataFromDB(object : DataBaseCallBack {
            override fun callBack() {
                setListView()
                dialog.dismiss()
            }

        })
    }

    private fun setView() {
        binding.tvItemName.text = viewModel.getItemName()
    }


    private fun setListView() {
        if (binding.lv.adapter == null) {
            binding.lv.adapter = StockListSizeAdapter()
        }
        (binding.lv.adapter as StockListSizeAdapter).setData(viewModel.getStockSizeData())
        (binding.lv.adapter as StockListSizeAdapter).notifyDataSetChanged()
    }


}