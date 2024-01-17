package com.example.seoulf3.input

import android.content.Intent
import android.os.Bundle
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

    interface CallbackPosition {
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
        setContentView(binding.root)
        viewModel.getItemNameDataFromDatabase(object : DataBaseCallBack {
            override fun callBack() {
                dialog.dismiss()
                setListView()
                setOnClick()
            }
        })
    }

    val FROM_SIZE = 0
    val FROM_SCAN = 1
    val FROM_QUANTITY = 2


    fun setOnClick() {

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.lv.setOnItemClickListener { _, _, i, _ ->
            val itemName = viewModel.getItemNameByIndex(i)
            val itemCategoryCode = viewModel.getItemCategoryCodeByIndex(i)
            val itemSizeCode = viewModel.getItemSizeCodeByIndex(i)
            val chooseItemInfo = viewModel.getChooseItem()
            chooseItemInfo.itemName = itemName
            chooseItemInfo.itemCategoryCode = itemCategoryCode
            chooseItemInfo.itemSizeCode = itemSizeCode
            viewModel.setChooseItem(chooseItemInfo)
            val intent = Intent(this@InputItemNameListActivity, InputItemSizeActivity::class.java)
            intent.putExtra("name", itemName)
            intent.putExtra("sizeCode", itemSizeCode)
            startActivityForResult(intent, FROM_SIZE)
        }
    }

    fun setListView() {
        if (binding.lv.adapter == null) {
            binding.lv.adapter = InputItemNameListAdapter()
        }
        (binding.lv.adapter as InputItemNameListAdapter).setData(viewModel.getItemNameList())
        (binding.lv.adapter as InputItemNameListAdapter).notifyDataSetChanged()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {

            if (requestCode == FROM_SIZE) {
                //todo 수량 입력후
                val chooseItemSize = data!!.getStringExtra("size").toString()
                val chooseItem = viewModel.getChooseItem()
                chooseItem.itemSize = chooseItemSize
                viewModel.setChooseItem(chooseItem)
                //todo 추천 자리 가져오기
                dialog.show()
                viewModel.getRecommendPosition(object : CallbackPosition {
                    override fun callBack(position: String) {
                        dialog.dismiss()
                        val chooseItem = viewModel.getChooseItem()
                        val intent = Intent(
                            this@InputItemNameListActivity,
                            InputPositionScanActivity::class.java
                        )
                        chooseItem.itemRecommendPosition = position
                        viewModel.setChooseItem(chooseItem)

                        intent.putExtra("name", chooseItem.itemName)
                        intent.putExtra("size", chooseItem.itemSize)
                        intent.putExtra("recommend", chooseItem.itemRecommendPosition)
                        startActivityForResult(intent, FROM_SCAN)
                    }
                })


            } else if (requestCode == FROM_SCAN) {
            //todo 스캔 후
                val code = data!!.getStringExtra("barcode").toString()
                val chooseItemInfo = viewModel.getChooseItem()
                chooseItemInfo.itemPosition = code
                viewModel.setChooseItem(chooseItemInfo)

                val intent = Intent(this@InputItemNameListActivity, InputQuantityActivity::class.java)
                intent.putExtra("name", chooseItemInfo.itemName)
                intent.putExtra("size", chooseItemInfo.itemSize)
                intent.putExtra("position", code)
                startActivityForResult(intent, FROM_QUANTITY)

            } else if (requestCode == FROM_QUANTITY) {
            //todo 수량 입력후
                val quantity = data!!.getStringExtra("quantity").toString()
                val chooseItem = viewModel.getChooseItem()
                chooseItem.itemQuantity = quantity
                viewModel.setChooseItem(chooseItem)
                dialog.show()
                viewModel.insertData(object : DataBaseCallBack {
                    override fun callBack() {
                        dialog.dismiss()
                        Toast.makeText(applicationContext, "입고 처리 되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                })
            }

        } else if (resultCode == RESULT_FIRST_USER) {
            //todo 재스캔
            val chooseItem = viewModel.getChooseItem()
            val intent = Intent(
                this@InputItemNameListActivity,
                InputPositionScanActivity::class.java
            )
            intent.putExtra("name", chooseItem.itemName)
            intent.putExtra("size", chooseItem.itemSize)
            intent.putExtra("recommend", chooseItem.itemRecommendPosition)
            startActivityForResult(intent, FROM_SCAN)

        } else {
            viewModel.setChooseItem(InputItemListViewModel.ChooseItemInfo())
            //todo 끝냄
        }
    }
}