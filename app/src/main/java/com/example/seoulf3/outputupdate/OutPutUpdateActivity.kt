package com.example.seoulf3.outputupdate

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.R
import com.example.seoulf3.databinding.ActivityOutPutUpdateBinding

class OutPutUpdateActivity : AppCompatActivity() {
    private lateinit var dialog: AlertDialog
    private lateinit var viewModel: OutPutUpdateViewModel
    private lateinit var binding: ActivityOutPutUpdateBinding
    interface CallBackDelete {
        fun callBack(p1: Int, p2: Int)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@OutPutUpdateActivity)
        }

        dialog.show()

        if (!::viewModel.isInitialized) {
            viewModel =
                ViewModelProvider(this@OutPutUpdateActivity)[OutPutUpdateViewModel::class.java]
        }

        if (!::binding.isInitialized) {
            binding = ActivityOutPutUpdateBinding.inflate(layoutInflater)
            viewModel.setDate()
        }
        viewModel.getDataFromDB(object : DataBaseCallBack {
            override fun callBack() {
                dialog.dismiss()
                setView()
            }

        })
        setContentView(binding.root)
        setOnClick()
    }

    fun setView() {
        if (binding.lv.adapter == null) {
            binding.lv.setAdapter(OutPutUpdateListAdapter(object : CallBackDelete {
                override fun callBack(p1: Int, p2: Int) {
                    //todo child 데이터 삭제후 다시 뷰 데이터 초기화
                }
            }))
        }
        binding.lv.setGroupIndicator(null)
        for (i in 0 until (binding.lv.expandableListAdapter as OutPutUpdateListAdapter).groupCount) {
            binding.lv.expandGroup(i)
        }
        (binding.lv.expandableListAdapter as OutPutUpdateListAdapter).setParents(viewModel.getItemNameList())
        (binding.lv.expandableListAdapter as OutPutUpdateListAdapter).setChild(viewModel.getChildList())
        (binding.lv.expandableListAdapter as OutPutUpdateListAdapter).notifyDataSetChanged()
    }

    fun setOnClick() {
        binding.btnBack.setOnClickListener {
            showDialogNotion()
        }
        binding.lv.setOnGroupClickListener { p0, p1, p2, p3 -> TODO("Not yet implemented") }
    }


    fun showDialogNotion() {
        val dialog = AlertDialog.Builder(this@OutPutUpdateActivity)
            .setTitle("알림")
            .setMessage("저장 하시겠습니까?")
            .setNegativeButton("아니요"
            ) { p0, p1 -> finish() }
            .setPositiveButton("예") {p0, p1 ->
                //todo 저장하기
                finish() }.create()
        if (viewModel.dataCheck()) {
            finish()
        } else {
            dialog.show()
        }
    }

    override fun onBackPressed() {
        showDialogNotion()
    }
}