package com.example.seoulf3.input

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.databinding.ActivityInputItemNameListBinding
import com.example.seoulf3.input.codescan.InputPositionScanActivity
import com.example.seoulf3.input.inputitemsize.InputItemSizeActivity
import com.example.seoulf3.input.inputquantity.InputQuantityActivity

class InputItemNameListActivity : AppCompatActivity() {
    //todo 이름 -> 사이즈 -> 바코드 -> 수량
    private lateinit var viewModel: InputItemListViewModel
    private lateinit var binding: ActivityInputItemNameListBinding
    private lateinit var dialog: android.app.AlertDialog

    interface RecommendCallBack {
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

        dialog.show()

        viewModel.getDateFromDatabase(object : DataBaseCallBack {
            override fun callBack() {
                dialog.dismiss()
                setView()
                setOnClick()
            }
        })
        setContentView(binding.root)

    }


    fun setView() {
        if (binding.lv.adapter == null) {
            binding.lv.adapter = InputItemNameListAdapter()
        }
        (binding.lv.adapter as InputItemNameListAdapter).setData(viewModel.getItemNameList())
        (binding.lv.adapter as InputItemNameListAdapter).notifyDataSetChanged()
    }

    val FROM_SELECT_SIZE = 1
    val FROM_SCAN = 2
    val FROM_QUANTITY = 3
    fun setOnClick() {
        binding.btnBack.setOnClickListener { finish() }
        binding.lv.setOnItemClickListener { _, _, i, _ ->
            viewModel.resetData()
            viewModel.setItemNameByIndex(i)
            viewModel.setItemSizeCodeByIndex(i)
            viewModel.setItemCategoryCodeByIndex(i)
            val intent = Intent(this@InputItemNameListActivity, InputItemSizeActivity::class.java)
            intent.putExtra("name", viewModel.getChooseItemName())
            intent.putExtra("size", viewModel.getChooseItemSizeCode())
            intent.putExtra("category", viewModel.getChooseItemCategoryCode())
            startActivityForResult(intent, FROM_SELECT_SIZE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == FROM_SELECT_SIZE) {
                val chooseSize = data!!.getStringExtra("size")
                viewModel.setChooseItemSize(chooseSize.toString())
                val intent = Intent(this@InputItemNameListActivity, InputPositionScanActivity::class.java)
                intent.putExtra("name", viewModel.getChooseItemName())
                intent.putExtra("size", viewModel.getChooseSize())
                viewModel.getRecommendPosition(object : RecommendCallBack {
                    override fun callBack(position: String) {
                        intent.putExtra("recommend", position)
                        startActivityForResult(intent, FROM_SCAN)
                    }
                })

            }

            if(requestCode == FROM_SCAN) {
                //todo 스캔 후
                val code = data!!.getStringExtra("barcode").toString()
                viewModel.setChoosePosition(code)
                val intent = Intent(this@InputItemNameListActivity, InputQuantityActivity::class.java)
                intent.putExtra("name", viewModel.getChooseItemName())
                intent.putExtra("position", code)
                intent.putExtra("size", viewModel.getChooseSize())
                startActivityForResult(intent,FROM_QUANTITY)
            }

            if (requestCode == FROM_QUANTITY) {
                //todo insert
                val q = data!!.getStringExtra("quantity").toString()
                viewModel.setChooseQuantity(q)
                viewModel.insertData(object : DataBaseCallBack {
                    override fun callBack() {
                        Toast.makeText(applicationContext, "입고 처리 되었습니다.", Toast.LENGTH_LONG).show()
                    }
                })
                viewModel.resetData()
            }

        } else if (resultCode == RESULT_FIRST_USER) {
            //todo 다시스캔
            val intent = Intent(this@InputItemNameListActivity, InputPositionScanActivity::class.java)
            intent.putExtra("name", viewModel.getChooseItemName())
            intent.putExtra("size", viewModel.getChooseSize())
            intent.putExtra("recommend", viewModel.getRecommendPosition())
            startActivityForResult(intent, FROM_SCAN)
        } else {
            //todo 취소
            viewModel.resetData()
        }
    }
}