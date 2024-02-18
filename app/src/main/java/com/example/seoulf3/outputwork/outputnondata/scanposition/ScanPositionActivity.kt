package com.example.seoulf3.outputwork.outputnondata.scanposition

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.databinding.ActivityScanPositionBinding
import com.example.seoulf3.outputwork.outputnondata.scanposition.quantity.QuantityActivity

class ScanPositionActivity : AppCompatActivity() {
    private lateinit var viewModel: ScanPositionViewModel
    private lateinit var binding: ActivityScanPositionBinding


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
        viewModel.setItemCategory(itemCategoryCode)
        viewModel.setItemCode(itemCode)
        viewModel.setMaxQ(maxQ)
        Toast.makeText(applicationContext, maxQ, Toast.LENGTH_SHORT).show()
        setContentView(binding.root)
        binding.lay.visibility = View.VISIBLE
        setView()

        viewModel.getDataFromDatabase(object : DataBaseCallBack {
            override fun callBack() {
                delayView()
                binding.rePosiiton.text = viewModel.getNowPosition()
                startScan()
            }
        })
    }

    fun setOnClick() {
        binding.btnRestart.setOnClickListener {
            //todo 리스타트
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
                    startActivityForResult(intent, 8)

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
        binding.lay.visibility = View.VISIBLE
        setView()
        //todo 딜레이
        Handler().postDelayed(Runnable {
            binding.lay.visibility = View.GONE
        }, 3000)

        goToNextPosition()
    }

    fun setView() {
        binding.tvMax.text = viewModel.getMaxQ()
        binding.tvNum.text = viewModel.getOutputQ()
    }

    fun goToNextPosition() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            //todo 다음 데이터
        } else if(resultCode == RESULT_FIRST_USER) {
            //todo 수량 오류
        } else {
            finish()
        }
    }
}