package com.example.seoulf3.input.inputitemsize

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.R
import com.example.seoulf3.databinding.ActivityInputItemSizeBinding

class InputItemSizeActivity : AppCompatActivity() {
    private lateinit var viewModel: InputItemSizeViewModel
    private lateinit var binding: ActivityInputItemSizeBinding
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::viewModel.isInitialized) {
            viewModel =
                ViewModelProvider(this@InputItemSizeActivity)[InputItemSizeViewModel::class.java]
        }

        if (!::binding.isInitialized) {
            binding = ActivityInputItemSizeBinding.inflate(layoutInflater)
        }

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@InputItemSizeActivity)
        }
        val name = intent.getStringExtra("name")
        val code = intent.getStringExtra("code")
        viewModel.setItemName(name.toString())
        viewModel.setItemCode(code.toString())
        dialog.show()
        viewModel.getItemSizeListFromDatabase(object : DataBaseCallBack {
            override fun callBack() {
                setListView()
                setOnClick()
                dialog.dismiss()
            }
        })
        setContentView(binding.root)
        setView()
    }

    private fun setOnClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.lv.setOnItemClickListener { adapter, _, i, _ ->

            val size = adapter.getItemAtPosition(i).toString()
            intent.putExtra("size", size)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun setView() {
        binding.tvItemName.text = viewModel.getItemName()
    }

    private fun setListView() {
        if (binding.lv.adapter == null) {
            binding.lv.adapter = InputItemSizeAdapter()
            (binding.lv.adapter as InputItemSizeAdapter).setSizeListData(viewModel.getItemNameList())
            (binding.lv.adapter as InputItemSizeAdapter).notifyDataSetChanged()
        }
        (binding.lv.adapter as InputItemSizeAdapter).setSizeListData(viewModel.getItemNameList())

    }
}