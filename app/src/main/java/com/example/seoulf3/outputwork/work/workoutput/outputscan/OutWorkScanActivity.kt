package com.example.seoulf3.outputwork.work.workoutput.outputscan

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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


    fun showDialog() {
        AlertDialog.Builder(this@OutWorkScanActivity)
            .setTitle("알림")
            .setMessage("모든 작업을 중지하고 뒤로 가시겠습니까?")
            .setPositiveButton("예"
            ) { p0, p1 -> setResult(RESULT_CANCELED)
            finish()
            }
            .setNegativeButton("아니요"
            ) { p0, p1 ->
            }
            .create().show()

    }
    override fun onBackPressed() {
        showDialog()
    }
}