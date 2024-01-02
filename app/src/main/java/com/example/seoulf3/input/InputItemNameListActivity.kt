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
    lateinit var viewModel: InputItemListViewModel
    lateinit var binding: ActivityInputItemNameListBinding
    lateinit var dialog: android.app.AlertDialog


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
            dialog.show()
        }

        setContentView(binding.root)

        viewModel.getPositionDataFromDB(object : DataBaseCallBack {
            override fun callBack() {
                viewModel.getItemListFromPosition(object : DataBaseCallBack {
                    override fun callBack() {
                        viewModel.getQuantityDataFromDB(object : DataBaseCallBack {
                            override fun callBack() {
                                setView()
                                setOnClick()
                            }
                        })
                    }
                })
            }
        })
    }

    private fun setView() {
        if (binding.lv.adapter == null) {
            binding.lv.adapter = InputItemNameListAdapter()
        }
        viewModel.getCategoryDataFromDB(object : DataBaseCallBack {
            override fun callBack() {
                Log.e("ccccc1", "4")
                (binding.lv.adapter as InputItemNameListAdapter).setItemListData(viewModel.getItemList())
                (binding.lv.adapter as InputItemNameListAdapter).notifyDataSetChanged()
                dialog.dismiss()
            }
        })
    }


    private fun setOnClick() {
        binding.btnBack.setOnClickListener { finish() }
        binding.lv.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this@InputItemNameListActivity, InputItemSizeActivity::class.java)
            intent.putExtra("name", viewModel.getCategorySizeCode(i).name)
            intent.putExtra("size", viewModel.getCategorySizeCode(i).sizeCode)
            startActivityForResult(intent, 8)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 8) {
                //todo 자리 바코드 띄우기 -> 수량 입력 (추천위치 알려주기 )
                val intent = data!!
                val barcodeIntent =
                    Intent(this@InputItemNameListActivity, InputPositionScanActivity::class.java)
                barcodeIntent.putExtra("name", intent.getStringExtra("name"))
                barcodeIntent.putExtra("size", intent.getStringExtra("size"))
                viewModel.setItemName(intent.getStringExtra("name").toString())
                viewModel.setSize(intent.getStringExtra("size").toString())
                barcodeIntent.putExtra("recommend", viewModel.getRecommendPosition())
                startActivityForResult(barcodeIntent, 7)
            } else if (requestCode == 7) {
                //todo 바코드 스캔후
                viewModel.setPosition(data!!.getStringExtra("barcode").toString())
                val intent =
                    Intent(this@InputItemNameListActivity, InputQuantityActivity::class.java)
                intent.putExtra("name", viewModel.getItemName())
                intent.putExtra("size", viewModel.getSize())
                intent.putExtra("position", viewModel.getPosition())
                startActivityForResult(intent, 6)
            } else if (requestCode == 6) {
                //todo 수량 입력 후
                if (resultCode == RESULT_FIRST_USER) {
                    val barcodeIntent =
                        Intent(
                            this@InputItemNameListActivity,
                            InputPositionScanActivity::class.java
                        )
                    barcodeIntent.putExtra("name", viewModel.getItemName())
                    barcodeIntent.putExtra("size", viewModel.getSize())
                    barcodeIntent.putExtra("recommend", viewModel.getRecommendPosition())
                    startActivityForResult(barcodeIntent, 7)
                } else {
                    val intent = data!!
                    val quantity = intent.getStringExtra("quantity").toString()
                    viewModel.setQuantity(quantity)
                    viewModel.setItemAtDataBase()
                }
            }
        } else {

        }
    }

}