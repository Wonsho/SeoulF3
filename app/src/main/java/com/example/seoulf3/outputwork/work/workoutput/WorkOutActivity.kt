package com.example.seoulf3.outputwork.work.workoutput

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.media.tv.interactive.TvInteractiveAppService
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

    val FROM_SCAN = 2

    val FROM_Q = 3
    fun startScan() {
        viewModel.getDataAtPosition(object : DataBaseCallBack {
            override fun callBack() {
                val itemPosition = viewModel.getNowItemPosition()
                val intent = Intent(this@WorkOutActivity, OutWorkScanActivity::class.java)
                intent.putExtra("position", itemPosition)
                startActivityForResult(intent, FROM_SCAN)
            }

        })
    }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

         if (resultCode == RESULT_OK) {

             if (requestCode == FROM_SCAN) {
                 //todo 수량체크
                 val intent = Intent(this@WorkOutActivity, OutputCheckActivity::class.java)
                 val itemName = viewModel.getSelectedItemName()
                 val itemSize = viewModel.getSelectedItemSize()
                 val position = viewModel.getSelectedItemPosition()
                 val rQ = viewModel.getReleaseQ()

                 intent.putExtra("name", itemName)
                 intent.putExtra("position", position)
                 intent.putExtra("size", itemSize)
                 intent.putExtra("releaseQ", rQ)
                 intent.putExtra("positionQ", viewModel.getNowItemPositionQuantity())

                 startActivityForResult(intent, FROM_Q)
             }

             if (requestCode == FROM_Q) {
                 //todo 갯수가 현재 재고와 맞을경우 현 포지션 삭제

             }
         }

    }
}