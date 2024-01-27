package com.example.seoulf3.outputupdate.updatesize

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.R
import com.example.seoulf3.databinding.ActivityOutPutUpadateSizeBinding
import com.example.seoulf3.outputupdate.OutPutUpdateActivity
import com.example.seoulf3.searchstock.searchstocksize.StockListSizeAdapter
import com.example.seoulf3.outputupdate.updatesize.quantity.QuantityActivity

class OutPutUpdateSizeActivity : AppCompatActivity() {

    private lateinit var viewModel: UpdateSizeViewModel
    private lateinit var dialog: AlertDialog
    private lateinit var binding: ActivityOutPutUpadateSizeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProvider(this)[UpdateSizeViewModel::class.java]
        }

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@OutPutUpdateSizeActivity)
        }

        if (!::binding.isInitialized) {
            binding = ActivityOutPutUpadateSizeBinding.inflate(layoutInflater)
        }

        val sizeCode = intent.getStringExtra("sizeCode")
        val category = intent.getStringExtra("categoryCode")
        val itemName = intent.getStringExtra("name")

        binding.tvItemName.text = itemName.toString()
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
            binding.lv.adapter = UpdateSizeListAdapter()
        }
        (binding.lv.adapter as UpdateSizeListAdapter).setParentsData(viewModel.getItemSizeList())
        (binding.lv.adapter as UpdateSizeListAdapter).setChildData(viewModel.getItemSizeMapQ())
        (binding.lv.adapter as UpdateSizeListAdapter).notifyDataSetChanged()

    }

    fun setOnClick() {
        binding.btnBack.setOnClickListener {
                setResult(RESULT_CANCELED)
                finish()
        }
        binding.lv.setOnItemClickListener { _, _, i, _ ->
            val itemName = viewModel.getItemName()
            val chooseItemSize = viewModel.getItemChooseItemSize(i)
            val maxQ = viewModel.getMaxQByIndex(i)
            if (maxQ == "-1") {
                Toast.makeText(applicationContext, "수량이 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@OutPutUpdateSizeActivity, QuantityActivity::class.java)
                intent.putExtra("name", itemName)
                intent.putExtra("maxQ", maxQ)
                intent.putExtra("size", chooseItemSize)
                startActivityForResult(intent, 8)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_CANCELED) {
            setResult(RESULT_CANCELED)
            finish()
        } else {
            if (requestCode == 8) {
                val intent = Intent(this@OutPutUpdateSizeActivity, OutPutUpdateActivity::class.java)
                intent.putExtra("name", data!!.getStringExtra("name"))
                intent.putExtra("size", data!!.getStringExtra("size"))
                intent.putExtra("category", viewModel.getItemCategoryCode())
                intent.putExtra("quantity", data!!.getStringExtra("quantity"))
                intent.putExtra(
                    "itemCode",
                    viewModel.getItemCode(data!!.getStringExtra("size").toString())
                )
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}