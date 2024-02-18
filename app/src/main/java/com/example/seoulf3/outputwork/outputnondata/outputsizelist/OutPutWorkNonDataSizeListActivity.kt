package com.example.seoulf3.outputwork.outputnondata.outputsizelist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.databinding.ActivityOutPutWorkNonDataSizeListBinding
import com.example.seoulf3.outputwork.outputnondata.inputquantity.InputQuantityActivity

class OutPutWorkNonDataSizeListActivity : AppCompatActivity() {

    private lateinit var viewModel: OutPutWorkNonDataSizeListViewModel
    private lateinit var binding: ActivityOutPutWorkNonDataSizeListBinding
    private lateinit var dialog: android.app.AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProvider(this)[OutPutWorkNonDataSizeListViewModel::class.java]
        }

        if (!::binding.isInitialized) {
            binding = ActivityOutPutWorkNonDataSizeListBinding.inflate(layoutInflater)
        }

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@OutPutWorkNonDataSizeListActivity)
        }

        val itemName = intent.getStringExtra("name").toString()
        val categoryCode = intent.getStringExtra("category").toString()
        val itemSizeCode = intent.getStringExtra("size").toString()

        viewModel.setItemName(itemName)
        viewModel.setItemCategoryCode(categoryCode)
        viewModel.setItemSizeCode(itemSizeCode)
        setView()

        viewModel.getDataFromDataBase(object : DataBaseCallBack {
            override fun callBack() {
                setListView()
                setOnClick()
            }

        })
        setContentView(binding.root)
    }

    private fun setView() {
        val itemName = viewModel.getItemName()
        binding.tvItemName.text = itemName
    }

    private fun setListView() {
        if (binding.lv.adapter == null) {
            binding.lv.adapter = OutPutWorkNonDataSizeAdapter()
        }

        ( binding.lv.adapter as OutPutWorkNonDataSizeAdapter).setSizeList(viewModel.getItemSizeList())
        ( binding.lv.adapter as OutPutWorkNonDataSizeAdapter).setQuantityData(viewModel.getItemSizeMapQuantity())
        ( binding.lv.adapter as OutPutWorkNonDataSizeAdapter).notifyDataSetChanged()
    }

    private fun setOnClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.lv.setOnItemClickListener { _, _, i, _ ->
            //todo -> 스캔 후 수량 입력
            val itemSize = viewModel.getSizeByIndex(i)
            val quantity = viewModel.checkQuantityByIndex(i)
            if (quantity == "0") {
                Toast.makeText(applicationContext, "재고가 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnItemClickListener
            }
            val itemCode = viewModel.getItemCodeByIndex(i)
            val itemName = viewModel.getItemName()
            val itemCategoryCode = viewModel.getItemCodeByIndex(i)

            val intent = Intent(this@OutPutWorkNonDataSizeListActivity, InputQuantityActivity::class.java)
            intent.putExtra("name", itemName)
            intent.putExtra("size", itemSize)
            intent.putExtra("quantity", quantity)
            intent.putExtra("itemCode", itemCode)
            intent.putExtra("category", itemCategoryCode)
            startActivity(intent)
        }
    }
}