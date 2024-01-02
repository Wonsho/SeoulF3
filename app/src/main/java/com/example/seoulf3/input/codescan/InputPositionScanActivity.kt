package com.example.seoulf3.input.codescan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.seoulf3.R
import com.example.seoulf3.databinding.ActivityInputPositionScanBinding
import com.example.seoulf3.databinding.ActivityStockBarcodeScanBinding

class InputPositionScanActivity : AppCompatActivity() {
    lateinit var binding: ActivityInputPositionScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::binding.isInitialized) {
            binding = ActivityInputPositionScanBinding.inflate(layoutInflater)
        }
        setContentView(binding.root)
        setTv()
        startScan()
    }

    private fun setTv() {
        val itemName = intent.getStringExtra("name").toString()
        val itemSize = intent.getStringExtra("size").toString()
        val recommend = intent.getStringExtra("recommend").toString()
        binding.rePosiiton.text = if (recommend == "") {
            "없음"
        } else {
            recommend
        }
        binding.tvItemName.text = itemName
        binding.tvItemSize.text = itemSize
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
                    startScan()
                } else {
                    intent.putExtra("barcode", result.toString())
                    setResult(RESULT_OK, intent)
                    finish()
                    pause()
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
}