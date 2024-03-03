package com.example.seoulf3.outputwork.work.workoutput

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.media.tv.interactive.TvInteractiveAppService
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.setViewTreeOnBackPressedDispatcherOwner
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

        binding.lay.visibility = View.VISIBLE


        viewModel.setDate(intent.getStringExtra("date").toString())
        setContentView(binding.root)

        viewModel.getDataFromDB(object : DataBaseCallBack {
            override fun callBack() {
                viewModel.getPositionDataFromDatabase(object : DataBaseCallBack {
                    override fun callBack() {
                        delayView()
                    }
                })
            }
        })
    }

    fun setOnClick() {
        binding.btnBack.setOnClickListener {
            //todo 작업 끝 다이로그
        }

        binding.btnNext.setOnClickListener {
            //todo 다음 작업
        }
    }


    fun delayView() {
        binding.lay.visibility = View.GONE
        binding.tvItemName.text = WorkOutViewModel.NowItem.getNowItemName() // 현재 아이템 이름
        binding.tvItemSize.text = WorkOutViewModel.NowItem.getNowItemSize()// 현재 아이템 사이즈
        binding.tvNeedItemQ.text = viewModel.outputDataInfoList.size.toString()// 현제 남은 아이템 갯수

        binding.tvItemQ.text = WorkOutViewModel.NowItem.getOriginReleaseQ().toString() // 현 아이템 출고 해야되는 개수
        binding.tvReleasedQ.text = WorkOutViewModel.NowItem.nowItemOriginReleaseQ.toString() // 현 아이템 출고 된 개수

        Toast.makeText(applicationContext, WorkOutViewModel.NowItem.dataPositionList.size.toString(),Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable {
            binding.lay.visibility = View.GONE
            //todo startScanActivity
            val intent = Intent(this@WorkOutActivity, OutWorkScanActivity::class.java)
//            startActivityForResult(intent, 8)
        }, 3000)
    }

}