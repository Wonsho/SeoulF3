package com.example.seoulf3.searchstock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.R
import com.example.seoulf3.databinding.ActivitySearchStockBinding
import com.example.seoulf3.searchstock.searchstocksize.SearchStockSizeActivity

class SearchStockActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchStockViewModel
    private lateinit var binding: ActivitySearchStockBinding
    private lateinit var dialog: android.app.AlertDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProvider(this)[SearchStockViewModel::class.java]
        }

        if (!::binding.isInitialized) {
            binding = ActivitySearchStockBinding.inflate(layoutInflater)
        }

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@SearchStockActivity)
        }
        setContentView(binding.root)
        dialog.show()
        setListView()
        setOnClick()
    }


    private fun setListView() {
        if (binding.lv.adapter == null) {
            binding.lv.adapter = SearchStockListAdapter()
        }

        viewModel.getCategoryDataFromDB(object : DataBaseCallBack {
            override fun callBack() {
                dialog.dismiss()
                (binding.lv.adapter as SearchStockListAdapter).setItemListData(viewModel.getItemList())
                (binding.lv.adapter as SearchStockListAdapter).notifyDataSetChanged()
            }
        })
    }

    private fun setOnClick() {
        binding.lv.setOnItemClickListener { adapterView, view, i, l ->
            val category = viewModel.getCategorySizeCode(i)
            val intent = Intent(this@SearchStockActivity, SearchStockSizeActivity::class.java)
            intent.putExtra("name", category.name)
            intent.putExtra("size", category.sizeCode)
            startActivity(intent)
        }
    }
}