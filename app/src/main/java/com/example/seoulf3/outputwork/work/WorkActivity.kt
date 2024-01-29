package com.example.seoulf3.outputwork.work

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.R
import com.example.seoulf3.databinding.ActivityOutPutWorkBinding
import com.example.seoulf3.databinding.ActivityWorkBinding
import com.example.seoulf3.outputwork.work.workoutput.WorkOutActivity


//todo 항목 보여주기
class WorkActivity : AppCompatActivity() {
    lateinit var dialog: android.app.AlertDialog
    lateinit var viewModel: WorkViewModel
    lateinit var binding: ActivityWorkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!::dialog.isInitialized) {
            binding = ActivityWorkBinding.inflate(layoutInflater)
        }

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@WorkActivity)
        }

        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProvider(this)[WorkViewModel::class.java]
        }
        dialog.show()

        val date = intent.getStringExtra("date").toString()
        viewModel.setItemDate(date)

        viewModel.getData(object : DataBaseCallBack {
            override fun callBack() {
                dialog.dismiss()
                setListView()
                setOnClick()
            }
        })

        setContentView(binding.root)
    }

    fun setOnClick() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnSave.setOnClickListener {
            //todo 출고 작업 시작
            val date = intent.getStringExtra("date").toString()
            val intent = Intent(this@WorkActivity, WorkOutActivity::class.java)
            intent.putExtra("date", date)
            startActivity(intent)
            Toast.makeText(applicationContext, "Click", Toast.LENGTH_SHORT).show()
        }
        binding.lv.setOnGroupClickListener { _, _, _, _ ->
            true
        }

    }

    fun setListView() {
        if (binding.lv.adapter == null) {
            binding.lv.setAdapter(WorkAdapter())
        }
        binding.lv.setGroupIndicator(null)
        (binding.lv.expandableListAdapter as WorkAdapter).setItemNameList(viewModel.getItemNameList())
        (binding.lv.expandableListAdapter as WorkAdapter).setSizeList(viewModel.getItemSizeList())
        (binding.lv.expandableListAdapter as WorkAdapter).notifyDataSetChanged()
        for (i in 0 until (binding.lv.expandableListAdapter as WorkAdapter).groupCount) {
            binding.lv.expandGroup(i)
        }
        binding.lv.isClickable = false
    }

}