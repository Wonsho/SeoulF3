package com.example.seoulf3.outputwork.work.workoutput.outputscan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.seoulf3.R
import com.example.seoulf3.databinding.ActivityOutWorkScanBinding

class OutWorkScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOutWorkScanBinding
    private var position = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::binding.isInitialized) {
            binding = ActivityOutWorkScanBinding.inflate(layoutInflater)
        }

        setContentView(binding.root)
        setTv()
        startScan()
        binding.btnRestart.setOnClickListener {
            startScan()
        }
    }

    private fun setTv() {
        position = intent.getStringExtra("position").toString()
        binding.rePosiiton.text = position


    }


    private fun startScan() {
        binding.qrScanner.apply {
            setStatusText("")
            decodeContinuous { result ->
                val intent = Intent()
                val barcode = result.toString()

                if (barcode != position) {

                    Toast.makeText(
                        applicationContext,
                        "잘못된 위치 입니다.\n$position 해당 자리를 스캔해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    intent.putExtra("position", barcode)
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