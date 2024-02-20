package com.example.seoulf3.outputwork.outputnondata.scanposition.quantity

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.seoulf3.R
import com.example.seoulf3.databinding.ActivityOutputCheckBinding
import com.example.seoulf3.outputwork.outputnondata.scanposition.ScanPositionActivity
import kotlin.math.max

class QuantityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOutputCheckBinding

    var position = ""
    var itemName = ""
    var itemSize = ""
    var outputtedQ = ""
    var maxQ = ""
    var qInPosition = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::binding.isInitialized) {
            binding = ActivityOutputCheckBinding.inflate(layoutInflater)
        }
        setContentView(binding.root)
        getData()
        setView()
        setOnClick()
    }

    fun setView() {
        binding.tvPosition.text = this.position
        binding.tvItemName.text = this.itemName
        binding.tvItemSize.text = this.itemSize

        val outputtedQ = outputtedQ.toInt()
        val maxQ = this.maxQ.toInt()

        val needQ = maxQ - outputtedQ
        binding.tvQuantity.text = needQ.toString()

        val pQ = this.qInPosition.toInt()

        if (pQ > needQ) {
            binding.tvMaxNum.text = needQ.toString()
        } else {
            binding.tvMaxNum.text = pQ.toString()
        }


    }

    fun getData() {
        position = intent.getStringExtra("position").toString()
        itemName = intent.getStringExtra("name").toString()
        itemSize = intent.getStringExtra("size").toString()
        outputtedQ = intent.getStringExtra("outtedQ").toString()
        maxQ = intent.getStringExtra("maxQ").toString()
        qInPosition = intent.getStringExtra("qInPosition").toString()

    }

    fun showErrorDialog() {
        AlertDialog.Builder(this@QuantityActivity)
            .setTitle("알림")
            .setMessage("현재 품목이 존재하지 않습니까?")
            .setPositiveButton("예") { p0, p1 ->
                //에러 입력
                intentOnlyError()
            }
            .setNegativeButton("아니요") { p0, p1 ->
                AlertDialog.Builder(this@QuantityActivity)
                    .setMessage("가능한 수량을 입력후 \n확인을 누르고\n재고이상을 눌러주세요.")
                    .setPositiveButton(
                        "확인"
                    ) { _, _ -> }
                    .setTitle("주의")
                    .create().show()
            }
            .create().show()
    }

    fun showDialogNotEnoughQ() {
        AlertDialog.Builder(this@QuantityActivity)
            .setTitle("알림")
            .setMessage("현위치 출고 가능한 수량보다 적습니다.\n다음작업으로 넘어가시겠습니까?")
            .setPositiveButton("예") { p0, p1 ->
                //다음 작업
                intentNowEnough()
            }
            .setNegativeButton("아니요") { p0, p1 ->
            }
            .setNeutralButton("재고 이상") { p0, p1 ->
                //재고 이상
                if (binding.tvNum.text == "0") {
                    intentOnlyError()
                } else {
                    intentWithError()
                }

            }
            .create().show()
    }

    fun setOnClick() {

        fun checkFirstZero() {
            if (binding.tvNum.text == "0") {
                binding.tvNum.text = ""
            }
        }

        fun inputNum(num: String) {
            checkFirstZero()
            val _num = binding.tvNum.text.toString()
            val inputN = _num + num

            if (inputN.trim().toInt() > binding.tvMaxNum.text.toString().toInt()) {
                Toast.makeText(applicationContext, "출고 가능 수량을 초과 하였습니다.", Toast.LENGTH_SHORT).show()
                binding.tvNum.text = binding.tvMaxNum.text.toString()
            } else {
                binding.tvNum.text = inputN

            }
        }

        binding.btnAddGoods.setOnClickListener {
            //todo 만약 수량이 요구 수량보다 적을경우 다이로그 띄우기
            if (binding.tvNum.text != binding.tvMaxNum.text) {
                //todo 다이로그 띄우기
                showDialogNotEnoughQ()
                return@setOnClickListener
            } else {
                intentData()
            }
        }
        binding.c.setOnClickListener {
            if (binding.tvNum.length() <= 1) {
                binding.tvNum.text = "0"
            } else {
                val num = binding.tvNum.text
                val input = num.substring(0, num.length - 1)
                binding.tvNum.text = input
            }
        }

        binding.c.setOnLongClickListener {
            binding.tvNum.text = "0"
            false
        }

        binding.n0.setOnClickListener { inputNum("0") }
        binding.n1.setOnClickListener { inputNum("1") }
        binding.n2.setOnClickListener { inputNum("2") }
        binding.n3.setOnClickListener { inputNum("3") }
        binding.n4.setOnClickListener { inputNum("4") }
        binding.n5.setOnClickListener { inputNum("5") }
        binding.n6.setOnClickListener { inputNum("6") }
        binding.n7.setOnClickListener { inputNum("7") }
        binding.n8.setOnClickListener { inputNum("8") }
        binding.n9.setOnClickListener { inputNum("9") }

        binding.btnError.setOnClickListener {
            //todo 다이로그 띄우기
            showErrorDialog()
        }
    }

    val result = ScanPositionActivity.Result
    fun intentData() {
        //모든 데이터 보냄
        val result = this.result.DATA
        val q = binding.tvNum.text.toString()
        intent.putExtra("q", q)
        setResult(result)
        finish()
    }

    fun intentNowEnough() {
        //충분치 않는 데이터
        val result = this.result.DATA_NOT_ENOUGH
        val q = binding.tvNum.text.toString()
        intent.putExtra("q", q)
        setResult(result)
        finish()
    }

    fun intentWithError() {
        //에러와 함께 충분치 않은 데이터
        val result = this.result.DATA_WITH_ERROR
        val q = binding.tvNum.text.toString()
        intent.putExtra("q", q)
        setResult(result)
        finish()
    }

    fun intentOnlyError() {
        //오직 에러만 보내기
        val result = this.result.ONLY_ERROR
        val q = binding.tvNum.text.toString()
        intent.putExtra("q", q)
        setResult(result)
        finish()
    }

    fun showDialog() {
        AlertDialog.Builder(this@QuantityActivity)
            .setTitle("알림")
            .setMessage("모든 작업을 중지하고 뒤로가시겠습니까?")
            .setPositiveButton("예") { p0, p1 ->
                setResult(RESULT_CANCELED)
                finish()
            }
            .setPositiveButton("아니요") { p0, p1 -> }
            .create().show()

    }

    override fun onBackPressed() {
        showDialog()
    }
}