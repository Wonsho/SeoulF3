package com.example.seoulf3.checkstock.scan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.seoulf3.databinding.ActivityStockBarcodeScanBinding

class StockBarcodeScanActivity : AppCompatActivity() {
    lateinit var binding: ActivityStockBarcodeScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!::binding.isInitialized) {
            binding = ActivityStockBarcodeScanBinding.inflate(layoutInflater)
        }
        setContentView(binding.root)
        setTv()
        startScan()
    }

    private fun setTv() {
        binding.tvInout.text = intent.getStringExtra("InOrOut")
    }

    private fun startScan() {
        binding.qrScanner.apply {
            setStatusText("")
            decodeContinuous { result ->
                // TODO 스캔결과 처리
                val intent = Intent()

                val barcode = result.toString()
                if (!(barcode[0] in 'A'..'Z')) {
                    Toast.makeText(context, "잘못된 위치코드 입니다.\n$barcode", Toast.LENGTH_LONG).show()
                    setResult(RESULT_CANCELED)
                } else {
                    intent.putExtra("barcode", result.toString())
                    setResult(RESULT_OK, intent)
                }
                finish()
                pause()
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

}
