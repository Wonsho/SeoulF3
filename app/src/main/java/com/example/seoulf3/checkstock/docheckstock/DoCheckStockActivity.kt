package com.example.seoulf3.checkstock.docheckstock

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.DataBaseCallBack
import com.example.seoulf3.checkstock.quantity.DoCheckQuantityActivity
import com.example.seoulf3.databinding.ActivityDoCheckStockBinding
import com.example.seoulf3.outputwork.outputnondata.scanposition.ScanPositionActivity
import com.example.seoulf3.outputwork.outputnondata.scanposition.quantity.QuantityActivity

class DoCheckStockActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoCheckStockBinding
    private lateinit var viewModel: DoCheckStockViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::binding.isInitialized) {
            binding = ActivityDoCheckStockBinding.inflate(layoutInflater)
        }

        if (!::viewModel.isInitialized) {
            viewModel =
                ViewModelProvider(this@DoCheckStockActivity)[DoCheckStockViewModel::class.java]
        }
        binding.layDefault.visibility = View.VISIBLE

        setData()
        setContentView(binding.root)

        viewModel.getDataFromDataBase(object : DataBaseCallBack {
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
        onPause()
        onResume()
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
                    startScan()
                } else {
                    val intent =
                        Intent(this@DoCheckStockActivity, DoCheckQuantityActivity::class.java)
                    intent.putExtra("name", viewModel.getItemName())
                    intent.putExtra("size", viewModel.getItemSize())
                    intent.putExtra("position", viewModel.getNowPosition())
                    intent.putExtra("qInp", viewModel.getNowPositionQ())
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

        val outputQ = viewModel.getNowData()
        val maxQ = viewModel.getMaxData()
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


    fun setData() {
        val itemName = getIntent().getStringExtra("name").toString()
        val itemSize = intent.getStringExtra("size").toString()
        val itemCategory = intent.getStringExtra("category").toString()
        val itemCode = intent.getStringExtra("code").toString()

        viewModel.setItemName(itemName)
        viewModel.setItemSize(itemSize)
        viewModel.setItemCategory(itemCategory)
        viewModel.setItemCode(itemCode)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == 8) {
                binding.layDefault.visibility = View.VISIBLE
                val q = data!!.getStringExtra("q")!!
                val result = viewModel.inputData(q)
                if (result) {
                    viewModel.finishWork()
                    Toast.makeText(applicationContext, "모든 작업이 종료되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    delayView()
                }
            }
        } else {
            finish()
        }
    }

    fun showDialog() {
        AlertDialog.Builder(this@DoCheckStockActivity)
            .setTitle("알림")
            .setMessage("모든 작업을 중지하고 뒤로가시겠습니까?")
            .setPositiveButton(
                "예"
            ) { p0, p1 -> finish() }
            .setNegativeButton(
                "아니요"
            ) { p0, p1 ->

            }
            .create().show()
    }

    override fun onBackPressed() {
        //todo Show Dialog
        showDialog()
    }

}