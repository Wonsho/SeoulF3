package com.example.seoulf3.outputwork

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.R
import com.example.seoulf3.databinding.ActivityOutPutWorkBinding
import com.example.seoulf3.outputwork.outputnondata.OutPutWorkNonDataActivity
import com.example.seoulf3.outputwork.work.WorkActivity

//todo 출고 요청분이 있을 경우 실행 출고 작업 리스트 보여주기
class OutPutWorkActivity : AppCompatActivity() {

    interface DeleteCallBack {
        fun callBack(i: Int)
    }

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
                    val intent =
                        Intent(this@OutPutWorkActivity, OutPutWorkNonDataActivity::class.java)
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
        binding.btnBack.setOnClickListener { finish() }
        binding.lv.setOnItemClickListener { _, _, i, _ ->
            val intent = Intent(this@OutPutWorkActivity, WorkActivity::class.java)
            intent.putExtra("date", viewModel.getDateList()[i])
            startActivityForResult(intent, 3)
        }

    }

    private fun setView() {
        if (binding.lv.adapter == null) {
            binding.lv.adapter = OutputWorkAdapter(object : DeleteCallBack {
                override fun callBack(i: Int) {
                    Toast.makeText(applicationContext, "t", Toast.LENGTH_SHORT).show()
                }
            })
            (binding.lv.adapter as OutputWorkAdapter).setData(viewModel.getDateList())
            (binding.lv.adapter as OutputWorkAdapter).notifyDataSetChanged()
        }
    }

}