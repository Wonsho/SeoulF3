package com.example.seoulf3.outputupdate

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.R
import com.example.seoulf3.databinding.ActivityOutPutUpdateBinding

class OutPutUpdateActivity : AppCompatActivity() {
    private lateinit var dialog: AlertDialog
    private lateinit var viewModel: OutPutUpdateViewModel
    private lateinit var binding: ActivityOutPutUpdateBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@OutPutUpdateActivity)
        }

        if (!::viewModel.isInitialized) {
            viewModel =
                ViewModelProvider(this@OutPutUpdateActivity)[OutPutUpdateViewModel::class.java]
        }

        if (!::binding.isInitialized) {
            binding = ActivityOutPutUpdateBinding.inflate(layoutInflater)
        }
        viewModel.getItemCategory(object : DataBaseCallBack {
            override fun callBack() {
                setView()
            }

        })
        setContentView(binding.root)
    }

    fun setView() {
        if (binding.lv.adapter == null) {
            binding.lv.setAdapter(OutPutUpdateListAdapter())
        }
        (binding.lv.expandableListAdapter as OutPutUpdateListAdapter).setParentsData(viewModel.getItemNameList())
        (binding.lv.expandableListAdapter as OutPutUpdateListAdapter)

    }

    fun setOnClick() {

    }
}