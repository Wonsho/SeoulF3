package com.example.seoulf3.input

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.databinding.ActivityInputItemNameListBinding
import com.example.seoulf3.input.codescan.InputPositionScanActivity
import com.example.seoulf3.input.inputitemsize.InputItemSizeActivity
import com.example.seoulf3.input.inputquantity.InputQuantityActivity
import com.google.android.gms.common.internal.Objects.ToStringHelper

class InputItemNameListActivity : AppCompatActivity() {
    private lateinit var viewModel: InputItemListViewModel
    private lateinit var binding: ActivityInputItemNameListBinding
    private lateinit var dialog: android.app.AlertDialog

    interface RecommendPosition {
        fun callBack(position: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProvider(this)[InputItemListViewModel::class.java]
        }

        if (!::binding.isInitialized) {
            binding = ActivityInputItemNameListBinding.inflate(layoutInflater)
        }

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@InputItemNameListActivity)
        }
        setContentView(binding.root)
        dialog.show()
        viewModel.getDataFromDatabase(object : DataBaseCallBack {
            override fun callBack() {
                setView()
                setOnClick()
                dialog.dismiss()
            }
        })
    }


    private fun setView() {
        if (binding.lv.adapter == null) {
            binding.lv.adapter = InputItemNameListAdapter()
            (binding.lv.adapter as InputItemNameListAdapter).setItemListData(viewModel.getItemNameList())
            (binding.lv.adapter as InputItemNameListAdapter).notifyDataSetChanged()
        }
        (binding.lv.adapter as InputItemNameListAdapter).notifyDataSetChanged()
    }

    private fun setOnClick() {
        binding.btnBack.setOnClickListener { finish() }
        binding.lv.setOnItemClickListener { _, _, i, _ ->
            val itemName = viewModel.getItemNameInfoByIndex(i)
            val name = itemName.name
            val code = itemName.sizeCode

            val intent = Intent(this@InputItemNameListActivity, InputItemSizeActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("code", code)
            viewModel.setChooseItemName(name!!)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val size = data!!.getStringExtra("size").toString()
                viewModel.setChooseItemSize(size)
                dialog.show()
                viewModel.getRecommendPosition(object : RecommendPosition {
                    override fun callBack(position: String) {
                        dialog.dismiss()
                        val intent = Intent(
                            this@InputItemNameListActivity,
                            InputPositionScanActivity::class.java
                        )
                        intent.putExtra("recommend", position)
                        intent.putExtra("name", viewModel.getChooseItemName())
                        intent.putExtra("size", viewModel.getChooseItemSize())
                        startActivityForResult(intent, 3)
                    }

                })

            }
        }
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                //todo 바코드 잘 찍힘
                val code = data!!.getStringExtra("barcode")
                viewModel.setChooseItemPosition(code.toString())
                val intent =
                    Intent(this@InputItemNameListActivity, InputQuantityActivity::class.java)
                intent.putExtra("name", viewModel.getChooseItemName())
                intent.putExtra("size", viewModel.getChooseItemSize())
                intent.putExtra("position", viewModel.getChoosePosition())
                startActivityForResult(intent, 7)
            } else {
                //todo 뒤로 갔을때
            }
        }

        if (requestCode == 7) {
            if (resultCode == RESULT_OK) {
                viewModel.setInputItemQuantity(data!!.getStringExtra("quantity")!!)
                viewModel.insertItemData(object : DataBaseCallBack {
                    override fun callBack() {

                    }

                })
                Toast.makeText(applicationContext, "입고 처리 되었습니다.", Toast.LENGTH_LONG).show()
                //todo 수량 입력 끝
            } else if (resultCode == RESULT_FIRST_USER) {
                val intent =
                    Intent(this@InputItemNameListActivity, InputPositionScanActivity::class.java)
                intent.putExtra("recommend", viewModel.getRecommendPosition())
                intent.putExtra("name", viewModel.getChooseItemName())
                intent.putExtra("size", viewModel.getChooseItemSize())
                startActivityForResult(intent, 3)
            } else {

            }
        }
    }
}