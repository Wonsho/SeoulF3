package com.example.seoulf3.outputwork.outputnondata

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.databinding.ActivityOutPutWorkNonDataBinding
import com.example.seoulf3.databinding.ActivityWorkBinding
import com.example.seoulf3.outputwork.outputnondata.outputsizelist.OutPutWorkNonDataSizeListActivity
import com.example.seoulf3.outputwork.work.WorkViewModel


//todo 출고 요청분이 없을경우 실행
class OutPutWorkNonDataActivity : AppCompatActivity() {
    private lateinit var viewModel: OutPutWorkNonDataViewModel
    private lateinit var dialog: android.app.AlertDialog
    private lateinit var binding: ActivityOutPutWorkNonDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProvider(this)[OutPutWorkNonDataViewModel::class.java]
        }

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@OutPutWorkNonDataActivity)
        }

        if (!::binding.isInitialized) {
            binding = ActivityOutPutWorkNonDataBinding.inflate(layoutInflater)
        }
        setContentView(binding.root)
        viewModel.getDataFromDatabase(object : DataBaseCallBack {
            override fun callBack() {
                setView()
                setOnClick()
            }
        })
    }

    private fun setOnClick() {

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.lv.setOnItemClickListener { _, _, i, _ ->
            val itemName = viewModel.getItemByIndex(i)
            val itemCategory = viewModel.getItemCategoryCodeByIndex(i)
            val itemSizeCode = viewModel.getItemSizeCodeByIndex(i)

            val intent = Intent(
                this@OutPutWorkNonDataActivity,
                OutPutWorkNonDataSizeListActivity::class.java
            )
            intent.putExtra("name", itemName)
            intent.putExtra("size", itemSizeCode)
            intent.putExtra("category", itemCategory)
            startActivity(intent)
            finish()
        }
    }

    private fun setView() {
        if (binding.lv.adapter == null) {
            binding.lv.adapter = OutputWorkNonDataAdapter()
        }
        (binding.lv.adapter as OutputWorkNonDataAdapter).setItem(viewModel.getItemNameList())
        (binding.lv.adapter as OutputWorkNonDataAdapter).notifyDataSetChanged()

    }
}