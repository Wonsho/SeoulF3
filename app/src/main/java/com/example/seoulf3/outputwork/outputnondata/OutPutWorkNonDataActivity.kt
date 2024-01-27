package com.example.seoulf3.outputwork.outputnondata

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.databinding.ActivityOutPutWorkNonDataBinding


//todo 출고 요청분이 없을경우 실행
class OutPutWorkNonDataActivity : AppCompatActivity() {
    private lateinit var viewModel: OutPutWorkNonDataViewModel
    private lateinit var dialog: android.app.AlertDialog
    private lateinit var binding: ActivityOutPutWorkNonDataBinding

    interface CallBackInOutPut {
        fun callBack(nullCheck: Boolean)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProvider(this@OutPutWorkNonDataActivity)[OutPutWorkNonDataViewModel::class.java]
        }

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@OutPutWorkNonDataActivity)
        }

        if (!::binding.isInitialized) {
            binding = ActivityOutPutWorkNonDataBinding.inflate(layoutInflater)
        }

        setContentView(binding.root)
        getDataFromDB()
    }

    private fun setOnClick() {
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setListView() {
        if (binding.lv.adapter == null) {
            binding.lv.adapter = OutputWorkNonDataAdapter()
        }
    }

    private fun getDataFromDB() {
        viewModel.getDataFromDataBase(object : CallBackInOutPut {
            override fun callBack(nullCheck: Boolean) {
                if (nullCheck) {
                    //todo 데이터 널
                    Toast.makeText(applicationContext, "입고된 상품이 없습니다.", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    setListView()
                    setOnClick()
                }
            }

        })
    }
}