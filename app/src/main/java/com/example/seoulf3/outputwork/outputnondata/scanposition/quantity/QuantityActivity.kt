package com.example.seoulf3.outputwork.outputnondata.scanposition.quantity

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

    private lateinit var position: String
    private lateinit var itemName: String
    private lateinit var itemSize: String
    private lateinit var needQ: String
    private lateinit var quantityInPosition: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::binding.isInitialized) {
            binding = ActivityOutputCheckBinding.inflate(layoutInflater)
        }

        getSetData()
        setView()
        setOnClick()
        setContentView(binding.root)
    }


    fun getSetData() {

        val itemName = intent.getStringExtra("name").toString()
        val itemSize = intent.getStringExtra("size").toString()
        val position = intent.getStringExtra("position").toString()
        val needQ = intent.getStringExtra("needQ").toString()
        val positionQ = intent.getStringExtra("quantityInPosition").toString()
        this.itemName = itemName
        this.itemSize = itemSize
        this.position = position
        this.needQ = needQ
        this.quantityInPosition = positionQ

    }

    fun setView() {
        binding.tvPosition.text = this.position
        binding.tvItemName.text = this.itemName
        binding.tvItemSize.text = this.itemSize
        binding.tvQuantity.text = this.needQ
        val maxQ = this.needQ.toInt()
        val poQ = this.quantityInPosition.toInt()

        val quantity = if (maxQ >= poQ) {
            poQ.toString()
        } else {
            maxQ.toString()
        }
        binding.tvMaxNum.text = quantity

    }

    fun intentData() {
        setResult(RESULT_OK, intent.putExtra("inputQuantity", binding.tvNum.text.toString().trim()))
        finish()
    }

    fun intentError() {
        setResult(RESULT_FIRST_USER)
        finish()
    }

    fun intentErrorAndIntentQuantity() {
        setResult(RESULT_FIRST_USER, intent.putExtra("inputQuantity", binding.tvNum.text.toString().trim()))
        finish()
    }

    fun showDialog() {
        val dialog = AlertDialog.Builder(this@QuantityActivity)
            .setTitle("알림")
            .setMessage("요청 수량보다 적습니다.\n다음 작업으로 넘어가시겠습니까?")
            .setPositiveButton("예") { p0, p1 ->
                //todo 다음 작업
                intentData()
            }
            .setNegativeButton("아니요") { p0, p1 ->

            }
            .setNeutralButton("재고 이상") { p0, p1 ->
                //todo 재고 이상으로 등록
                intentErrorAndIntentQuantity()
            }
        dialog.create().show()
    }

    fun showErrorDialog() {
        val dialog = AlertDialog.Builder(this@QuantityActivity)
            .setTitle("알림")
            .setMessage("재고가 맞지 않습니까?")
            .setPositiveButton("예") { p0, p1 ->
                //todo 현재 수량에서 가져갈 수량 입력 후 에러 입력
                intentError()
            }
            .setNegativeButton("아니요") { p0, p1 ->

            }
        dialog.create().show()
    }

    fun showBackDialog() {
        val dialog = AlertDialog.Builder(this@QuantityActivity)
            .setTitle("알림")
            .setMessage("현 작업을 중지하고 뒤로 가시겠습니까?")
            .setPositiveButton("예") { p0, p1 ->
                setResult(RESULT_CANCELED)
                finish()
            }
            .setNegativeButton("아니요") { p0, p1 ->

            }
        dialog.create().show()
    }

    fun setOnClick() {

        binding.btnError.setOnClickListener {
            showErrorDialog()
        }

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
                showDialog()
                return@setOnClickListener
            }
            intentData()
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

    override fun onBackPressed() {
        showBackDialog()
    }
}