package com.example.seoulf3.input.inputitemsize

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            viewModel = ViewModelProvider(this)[InputItemSizeViewModel::class.java]
        }

        if (!::binding.isInitialized) {
            binding = ActivityInputItemSizeBinding.inflate(layoutInflater)
        }

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@InputItemSizeActivity)
            dialog.show()
        }

        viewModel.setItemName(intent.getStringExtra("name")!!)
        viewModel.setItemSizeCode(intent.getStringExtra("size")!!)
        binding.tvItemName.text = viewModel.getItemName()
        setView()
        setOnClick()
        setContentView(binding.root)
    }

    private fun setOnClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.lv.setOnItemClickListener { adapterView, view, i, l ->
            intent.putExtra("name", viewModel.getItemName())
            intent.putExtra("size", viewModel.getSizeList()[i])
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun setView() {
        if (binding.lv.adapter == null) {
            binding.lv.adapter = InputItemSizeAdapter()
        }
        viewModel.getSizeDataFromDB(object : DataBaseCallBack {
            override fun callBack() {
                dialog.dismiss()
                (binding.lv.adapter as InputItemSizeAdapter).setList(viewModel.getSizeList())
                (binding.lv.adapter as InputItemSizeAdapter).notifyDataSetChanged()
            }

        })
    }
}