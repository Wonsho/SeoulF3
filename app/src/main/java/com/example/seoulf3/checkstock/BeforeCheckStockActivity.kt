package com.example.seoulf3.checkstock

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.checkstock.docheckstock.DoCheckStockActivity
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
        viewModel.getErrorDataFromDatabase(object : DataBaseCallBack {
            override fun callBack() {
                setListView()
                setOnClick()
            }
        })
    }

    private fun setListView() {
        if (binding.lv.adapter == null) {
          binding.lv.adapter =  CheckStockAdapter()
        }

        (binding.lv.adapter as CheckStockAdapter).setListData(viewModel.getItemName())
        (binding.lv.adapter as CheckStockAdapter).notifyDataSetChanged()
    }

    private fun setOnClick() {
        binding.btnBack.setOnClickListener { finish() }
        binding.lv.setOnItemClickListener { _, _, i, _ ->
            //todo 해당 리스트 재고 조회
            val intent = Intent(this@BeforeCheckStockActivity, DoCheckStockActivity::class.java)
            val data = viewModel.getErrorDataByIndex(i)
            intent.putExtra("name", data.itemName)
            intent.putExtra("code", data.itemCode)
            intent.putExtra("category", data.categoryCode)
            intent.putExtra("size", data.itemSize)
            startActivity(intent)
            finish()
        }
    }


}