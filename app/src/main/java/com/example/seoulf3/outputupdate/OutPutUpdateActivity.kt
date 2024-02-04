package com.example.seoulf3.outputupdate

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.setViewTreeOnBackPressedDispatcherOwner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.DatabaseEnum
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.databinding.ActivityOutPutUpdateBinding
import com.example.seoulf3.outputupdate.updatesize.OutPutUpdateSizeActivity
import com.example.seoulf3.outputupdate.updatesize.UpdateSizeViewModel

class OutPutUpdateActivity : AppCompatActivity() {
    private lateinit var dialog: AlertDialog
    private lateinit var viewModel: OutPutUpdateViewModel
    private lateinit var binding: ActivityOutPutUpdateBinding
    interface CallBackDelete {
        fun callBack(p1: Int, p2: Int)
    }
    data class ChildData(var itemCode: String? = "", var itemName: String? = "", var itemSize: String? = "", var itemQ: String?  = "", var categoryCode: String = "")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (!::binding.isInitialized) {
            binding = ActivityOutPutUpdateBinding.inflate(layoutInflater)
        }

        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProvider(this)[OutPutUpdateViewModel::class.java]
        }

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@OutPutUpdateActivity)
        }
        dialog.show()
        setContentView(binding.root)
        viewModel.getDataFromDatabase(object : DataBaseCallBack {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun callBack() {
                dialog.dismiss()
                setListview()
                setOnClick()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkData() {
        if (viewModel.checkData) {
            val dialog = AlertDialog.Builder(this@OutPutUpdateActivity)
                .setTitle("알림")
                .setMessage("변경사항을 저장하고 나가시겠습니까?")
                .setPositiveButton(
                    "예"
                ) { p0, p1 ->
                    viewModel.insertAllData()
                    finish()
                }
                .setNegativeButton(
                    "아니요"
                ) { p0, p1 -> finish() }
            dialog.show()
        } else {
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBackPressed() {
        checkData()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setOnClick() {
        binding.btnSave.setOnClickListener {
            viewModel.insertAllData()
            Toast.makeText(applicationContext, "데이터가 저장되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.btnBack.setOnClickListener {
            checkData()
        }
        binding.lv.setOnGroupClickListener { _, _, i, _ ->
            val itemName = viewModel.getItemNameByIndex(i)
            val itemCategoryCode = viewModel.getItemCategoryCodeByIndex(i)
            val itemSizeCode = viewModel.getSizeCodeByIndex(i)
            Toast.makeText(applicationContext, itemCategoryCode, Toast.LENGTH_SHORT).show()

            val intent = Intent(this@OutPutUpdateActivity, OutPutUpdateSizeActivity::class.java )
            intent.putExtra("name", itemName)
            intent.putExtra("sizeCode", itemSizeCode)
            intent.putExtra("categoryCode", itemCategoryCode)
            startActivityForResult(intent,7)

            true
        }
    }

    private fun setListview() {

        binding.lv.setGroupIndicator(null)

        if (binding.lv.adapter == null) {
            binding.lv.setAdapter(OutPutUpdateListAdapter(object : CallBackDelete {
                override fun callBack(p1: Int, p2: Int) {
                    viewModel.deleteData(p1, p2)
                    setListview()
                    Toast.makeText(applicationContext, "삭제 되었습니다.", Toast.LENGTH_SHORT).show()
                }

            }))
        }
        for (i in 0 until (binding.lv.expandableListAdapter as OutPutUpdateListAdapter).groupCount) {
            binding.lv.expandGroup(i)
        }
        (binding.lv.expandableListAdapter as OutPutUpdateListAdapter).setParentsData(viewModel.getParentsData())
        (binding.lv.expandableListAdapter as OutPutUpdateListAdapter).setChildData(viewModel.getChildData())
        (binding.lv.expandableListAdapter as OutPutUpdateListAdapter).notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 7 && resultCode != RESULT_CANCELED) {
            //todo 데이터 날라옴 -> 리스트 수정
            viewModel.checkData = true
            val itemName = data!!.getStringExtra("name").toString()
            val itemSize = data!!.getStringExtra("size").toString()
            val itemQ = data!!.getStringExtra("quantity").toString()
            val itemCode = data!!.getStringExtra("itemCode").toString()
            val category = data!!.getStringExtra("category").toString()

            Toast.makeText(applicationContext, category, Toast.LENGTH_SHORT).show()
            val data = ChildData(itemCode, itemName, itemSize, itemQ, category)
            viewModel.insertDataInModel(data)
            setListview()
        }
    }
}