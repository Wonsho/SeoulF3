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

        dialog.show()
        setContentView(binding.root)

        viewModel.getDataFromDatabase(object : DataBaseCallBack {
            override fun callBack() {
                dialog.dismiss()
                setListView()
                setOnClick()
            }
        })

    }

    fun setOnClick() {
        binding.btnBack.setOnClickListener { finish() }
        binding.lv.setOnItemClickListener { _, _, i, _ ->
            //todo 해당 제고 파악
            val itemName = viewModel.getItemNameByIndex(i)
            val sizeCode = viewModel.getItemSizeCode(i)
            val categoryCode = viewModel.getItemCategoryCodeByIndex(i)

            val intent = Intent(this@SearchStockActivity, SearchStockSizeActivity::class.java)
            intent.putExtra("name", itemName)
            intent.putExtra("size", sizeCode)
            intent.putExtra("category", categoryCode)
            startActivity(intent)
        }
    }

    fun setListView() {
        if (binding.lv.adapter == null) {
            binding.lv.adapter = SearchStockListAdapter()
        }
        (binding.lv.adapter as SearchStockListAdapter).setData(viewModel.getItemNameList())
        (binding.lv.adapter as SearchStockListAdapter).notifyDataSetChanged()
    }
}