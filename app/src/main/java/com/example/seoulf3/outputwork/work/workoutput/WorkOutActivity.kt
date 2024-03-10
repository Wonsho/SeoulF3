package com.example.seoulf3.outputwork.work.workoutput

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.databinding.ActivityWorkOutBinding
import com.example.seoulf3.outputwork.outputnondata.scanposition.ScanPositionActivity
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

        viewModel.getDataOutputItemDataFromDatabase(object : DataBaseCallBack {
            override fun callBack() {
                viewModel.getPositionDataByItemCodeFromDatabase(object : DataBaseCallBack {
                    override fun callBack() {
                        delayView()
                    }

                })
            }
        })

    }

    /*남은 품목 개수
    * 현재 품목 이름
    * 현 품목 사이즈
    * */

    fun delayView() {
        binding.lay.visibility = View.GONE
        binding.tvNeedItemQ.text = viewModel.getNowNeedItemQ()
        binding.tvItemName.text = viewModel.getNowItemName()
        binding.tvItemSize.text = viewModel.getNowItemSize()
        binding.tvItemQ.text = viewModel.getNeedQ()
        binding.tvReleasedQ.text = viewModel.getOuttedQ()
        Handler().postDelayed(Runnable {
            val intent = Intent(this@WorkOutActivity, OutWorkScanActivity::class.java)
            intent.putExtra("position", viewModel.getNowItemPosition())
            startActivityForResult(intent, 8)
        }, 3500)
    }

    interface ResultCallBack {
        fun callBack(result: Int)
    }


    val resultS = ScanPositionActivity.Result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 8) {
            if (resultCode == RESULT_OK) {
                val intent = Intent(this@WorkOutActivity, OutputCheckActivity::class.java)
                val position = viewModel.getNowItemPosition()
                val itemSize = viewModel.getNowItemSize()
                val itemName = viewModel.getNowItemName()
                val needQ = viewModel.getNeedQ().toInt() - viewModel.getOuttedQ().toInt()
                val nowPositionItemSavedQ = viewModel.getNowPositionItemSavedQ()

                intent.putExtra("position", position)
                intent.putExtra("itemName", itemName)
                intent.putExtra("itemSize", itemSize)
                intent.putExtra("needQ", needQ.toString())
                intent.putExtra("positionSavedQ", nowPositionItemSavedQ)

                startActivityForResult(intent, 9)
            }

        } else if (requestCode == 9) {
            if (resultCode == ScanPositionActivity.Result.ONLY_ERROR) {
                //todo 오직 에러만
                viewModel.insertError(object : ResultCallBack {
                    override fun callBack(result: Int) {
                        if (result == resultS.NEXT_POSITION) {
                            //todo 다음 위치
                        } else if (result == resultS.NEXT_ITEM) {
                            //todo 다음 아이템
                        } else if (result == resultS.FINISH) {
                            //todo 완전히 종료
                        }
                    }
                })
            } else if (resultCode == ScanPositionActivity.Result.DATA_WITH_ERROR) {
                //todo 불충분 데이터와 에러
                val q = data!!.getStringExtra("q")!!
            } else if (resultCode == ScanPositionActivity.Result.DATA_NOT_ENOUGH) {
                //todo 불충분 데이터
                val q = data!!.getStringExtra("q")!!

            } else if (resultCode == ScanPositionActivity.Result.DATA) {
                //todo 데이터 넘어옴
                val q = data!!.getStringExtra("q")!!

            }
        }

        if (resultCode == RESULT_CANCELED) {
            finish()
        }
    }
}