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
import com.example.seoulf3.outputwork.work.workoutput.WorkOutViewModel

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
        Toast.makeText(applicationContext, itemCategoryCode, Toast.LENGTH_SHORT).show()

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
                    intent.putExtra("position", viewModel.getNowPosition())
                    intent.putExtra("name", viewModel.getItemName())
                    intent.putExtra("size", viewModel.getItemSize())
                    intent.putExtra("quantityInPosition", viewModel.getQuantityInPosition())
                    intent.putExtra("needQ", viewModel.getNeedQ())
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

    }

    fun setView() {
        binding.tvMax.text = viewModel.getMaxQ()
        binding.tvNum.text = viewModel.getOutputQ()
    }

    fun goToNextPosition() {
        binding.layDefault.visibility = View.GONE

        viewModel.nextPosition()

        if (viewModel.checkQuantity() || viewModel.getMaxCount() == "0") {
            //작업 끝
            Toast.makeText(applicationContext, "모든 출고 작업이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            finish()

        } else {
            setView()
            binding.rePosiiton.text = viewModel.getNowPosition()
            delayView()
            startScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            binding.layDefault.visibility = View.VISIBLE
            val intent = data!!
            val inputQ = intent.getStringExtra("inputQuantity").toString()
            viewModel.addOutputtedQ(inputQ)
            viewModel.replaceQuantityData(inputQ)
            viewModel.insertData(object : DataBaseCallBack {
                override fun callBack() {
                    goToNextPosition()
                }
            }, inputQ)

        } else if (resultCode == RESULT_FIRST_USER) {
            val intent = data
            if (intent != null) {
                val q = intent.getStringExtra("inputQuantity")

                if (!q.isNullOrBlank()) {
                    //todo 에러 입력 , 수량있음
                }

            } else {
                //todo 에러 입력, 수량 없음, 해당 수량 없음 다음 작업
            }
        } else {
            finish()
        }
    }
}