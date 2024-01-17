package com.example.seoulf3.outputwork

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.R
import com.example.seoulf3.databinding.ActivityOutPutWorkBinding
import com.example.seoulf3.outputwork.outputnondata.OutPutWorkNonDataActivity

//todo 출고 요청분이 있을 경우 실행
class OutPutWorkActivity : AppCompatActivity() {
    private lateinit var dialog: AlertDialog
    private lateinit var viewModel: OutPutWorkViewModel
    private lateinit var binding: ActivityOutPutWorkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@OutPutWorkActivity)
        }

        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProvider(this)[OutPutWorkViewModel::class.java]
        }

        if (!::binding.isInitialized) {
            binding = ActivityOutPutWorkBinding.inflate(layoutInflater)
        }
        dialog.show()
        setContentView(binding.root)
        viewModel.getDataFromDataBase(object : DataBaseCallBack {
            override fun callBack() {
                dialog.dismiss()
                if (viewModel.getDateList().isNullOrEmpty()) {
                    val intent = Intent(this@OutPutWorkActivity, OutPutWorkNonDataActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    setView()
                    setOnClick()
                }
            }
        })
    }

    private fun setOnClick() {

    }

    private fun setView() {

    }

}