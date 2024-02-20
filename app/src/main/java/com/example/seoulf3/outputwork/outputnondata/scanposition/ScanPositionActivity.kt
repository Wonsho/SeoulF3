package com.example.seoulf3.outputwork.outputnondata.scanposition

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.MainViewModel
import com.example.seoulf3.databinding.ActivityScanPositionBinding
import com.example.seoulf3.outputwork.outputnondata.scanposition.quantity.QuantityActivity
import com.example.seoulf3.outputwork.work.workoutput.WorkOutViewModel

class ScanPositionActivity : AppCompatActivity() {
    private lateinit var viewModel: ScanPositionViewModel
    private lateinit var binding: ActivityScanPositionBinding

    object Result {
        val ONLY_ERROR = 11
        val DATA_WITH_ERROR = 12
        val DATA = 13
        val DATA_NOT_ENOUGH = 14

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProvider(this)[ScanPositionViewModel::class.java]
        }

        if (!::binding.isInitialized) {
            binding = ActivityScanPositionBinding.inflate(layoutInflater)
        }

        val itemName = intent.getStringExtra("name").toString()
        val itemSize = intent.getStringExtra("size").toString()
        val itemCategoryCode = intent.getStringExtra("category").toString()
        val itemCode = intent.getStringExtra("itemCode").toString()
        val maxQ = intent.getStringExtra("quantity").toString()

        viewModel.setItemName(itemName)
        viewModel.setItemSize(itemSize)
        viewModel.setCategory(itemCategoryCode)
        viewModel.setItemCode(itemCode)
        viewModel.setMaxQ(maxQ)
        setContentView(binding.root)
        binding.layDefault.visibility = View.VISIBLE

        viewModel.getDataFromDatabase(object : DataBaseCallBack {
            override fun callBack() {
                delayView()
            }
        })
    }

    fun setOnClick() {
        binding.btnRestart.setOnClickListener {
            //todo 리스타트
            Toast.makeText(applicationContext, "RE", Toast.LENGTH_SHORT).show()
            onPause()
            onResume()
            startScan()
        }
    }

    private fun startScan() {
        binding.qrScanner.apply {
            setStatusText("")
            decodeContinuous { result ->
                val barcode = result.toString()
                if (barcode != viewModel.getNowPosition()) {
                    Toast.makeText(
                        applicationContext,
                        "잘못된 위치 입니다.\n${viewModel.getNowPosition()} 해당 자리를 스캔해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val intent = Intent(this@ScanPositionActivity, QuantityActivity::class.java)
                    // 포지션, 이름, 사아즈, 출고된 수량, 출고해야되는 수량, 현포지션 수량
                    val nowPosition = viewModel.getNowPosition()
                    val itemName = viewModel.getItemName()
                    val itemSize = viewModel.getItemSize()
                    val outputtedQ = viewModel.getOutputQ()
                    val maxQ = viewModel.getMaxQ()// 출고 해야되는 수량
                    val qInPosition = viewModel.getNowPositionQ()

                    intent.putExtra("position", nowPosition)
                    intent.putExtra("name", itemName)
                    intent.putExtra("size", itemSize)
                    intent.putExtra("outtedQ", outputtedQ)
                    intent.putExtra("maxQ", maxQ)
                    intent.putExtra("qInPosition", qInPosition)
                    startActivityForResult(intent, 8)
                    //todo 해당 포지션 맞음 수량 입력 인텐드 실행
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.qrScanner.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.qrScanner.pause()
    }


    fun delayView() {

        val outputQ = viewModel.getOutputQ()
        val maxQ = viewModel.getMaxQ()
        val nowPosition = viewModel.getNowPosition()

        binding.rePosiiton.text = nowPosition
        binding.tvMax.text = maxQ
        binding.tvNum.text = outputQ

        binding.layDefault.visibility = View.GONE
        binding.lay.visibility = View.VISIBLE

        Handler().postDelayed(Runnable {
            binding.lay.visibility = View.GONE
            startScan()
        }, 3000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_CANCELED) {
            finish()
            return
        }

        if (requestCode == 8) {
            if (resultCode == Result.DATA) {
                //데이터 충분
                val q = data!!.getStringExtra("q").toString()
            } else if (resultCode == Result.DATA_NOT_ENOUGH) {
                // 데이터 불풍분
                val q = data!!.getStringExtra("q").toString()

            } else if (resultCode == Result.DATA_WITH_ERROR) {
                //데이터 와 현수량 이상
                val q = data!!.getStringExtra("q").toString()

            } else if (resultCode == Result.ONLY_ERROR) {
                //현재 수량 이상
                //todo 현수량 없애고 에러 입력후 다음 데이터
            }
        }
    }
}