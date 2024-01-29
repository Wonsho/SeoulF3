package com.example.seoulf3.outputwork.work.workoutput

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.databinding.ActivityWorkOutBinding
import com.example.seoulf3.outputwork.work.workoutput.outputcheck.OutputCheckActivity
import com.example.seoulf3.outputwork.work.workoutput.outputscan.OutWorkScanActivity


class WorkOutActivity : AppCompatActivity() {
    private lateinit var viewModel: WorkOutViewModel
    private lateinit var dialog: AlertDialog
    private lateinit var binding: ActivityWorkOutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProvider(this)[WorkOutViewModel::class.java]
        }

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@WorkOutActivity)
        }

        if (!::binding.isInitialized) {
            binding = ActivityWorkOutBinding.inflate(layoutInflater)
        }

        viewModel.setDate(intent.getStringExtra("date").toString())

        viewModel.getDataFromDatabase(object : DataBaseCallBack {
            override fun callBack() {

                binding.lay.visibility = View.GONE
                //todo Scan

                setView()
                setOnClick()

                val intent = Intent(this@WorkOutActivity, OutWorkScanActivity::class.java)
                val itemData = viewModel.getDataByCount(viewModel.getDataCount().times)
                viewModel.setPositionData()
                val position = viewModel.getNowPosition()
                intent.putExtra("position", position)
                startActivityForResult(intent, 7)
            }
        })
        setContentView(binding.root)
    }

    fun showDialog() {
        AlertDialog.Builder(this@WorkOutActivity)
            .setTitle("알림")
            .setMessage("작업을 중단하고 뒤로 가시겠습니까?")
            .setPositiveButton("예", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    finish()
                }
            })
            .setNegativeButton("아니요", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                }
            })
            .create().show()
    }

    fun setOnClick() {
        binding.btnBack.setOnClickListener {
            showDialog()
        }

        binding.btnNext.setOnClickListener {
            //todo 다음 작업
        }
    }

    fun setView() {
        val count = viewModel.getDataCount()

        binding.tvSize.text = count.size.toString()
        binding.tvTimes.text = count.times.toString()

    }

    override fun onBackPressed() {
        showDialog()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == 7) {
                val position = data!!.getStringExtra("position").toString()
                val intent = Intent(this@WorkOutActivity, OutputCheckActivity::class.java)
                startActivityForResult(intent, 8)
            } else {

            }
        }
    }
}