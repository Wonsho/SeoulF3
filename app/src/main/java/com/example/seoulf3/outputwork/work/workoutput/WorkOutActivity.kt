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
        setContentView(binding.root)

        viewModel.getOutputData(object : DataBaseCallBack {
            override fun callBack() {
                binding.lay.visibility = View.GONE
                binding.tvSize.text = viewModel.getItemSize()
                binding.tvTimes.text = viewModel.getItemNowCount()
                startScan()
            }
        })
    }

    fun startScan() {
        viewModel.getDataAtPosition(object : DataBaseCallBack {
            override fun callBack() {

            }

        })
    }
}