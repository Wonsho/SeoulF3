package com.example.seoulf3.searchstock

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.databinding.ActivitySearchStockBinding
import com.example.seoulf3.searchstock.searchstocksize.SearchStockSizeActivity

class SearchStockActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchStockBinding
    private lateinit var viewModel: SearchStockViewModel
    private lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::binding.isInitialized) {
            binding = ActivitySearchStockBinding.inflate(layoutInflater)
        }

        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProvider(this)[SearchStockViewModel::class.java]
        }

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@SearchStockActivity)
        }

        setView()
        setOnClick()
        setContentView(binding.root)
    }

    fun setOnClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.lv.setOnItemClickListener { _, _, i, _ ->
            val intent = Intent(this@SearchStockActivity, SearchStockSizeActivity::class.java)
            intent.putExtra("sizeCode", viewModel.getSizeCodeByItemName(i))
            intent.putExtra("itemName", viewModel.getItemNameByIndex(i))
            startActivity(intent)
        }
    }

    fun setView() {
        if (binding.lv.adapter == null) {
            binding.lv.adapter = SearchStockListAdapter()
            viewModel.getDataFromDatabase(object : DataBaseCallBack {
                override fun callBack() {
                    (binding.lv.adapter as SearchStockListAdapter).setItem(viewModel.getItemNameList())
                    (binding.lv.adapter as SearchStockListAdapter).notifyDataSetChanged()
                }
            })
        }
        (binding.lv.adapter as SearchStockListAdapter).notifyDataSetChanged()
    }
}