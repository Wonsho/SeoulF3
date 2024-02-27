package com.example.seoulf3.checkstock.quantity

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.seoulf3.databinding.ActivityDoCheckQuantityBinding

class DoCheckQuantityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoCheckQuantityBinding
    private lateinit var itemName: String
    private lateinit var itemSize: String
    private lateinit var itemPosition: String
    private lateinit var qInp: String
    private var check = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::binding.isInitialized) {
            binding = ActivityDoCheckQuantityBinding.inflate(layoutInflater)
        }
        val itemName = intent.getStringExtra("name").toString()
        val itemSize = intent.getStringExtra("size").toString()
        val position = intent.getStringExtra("position").toString()
        val qInPo = intent.getStringExtra("qInp").toString()
        this.itemName = itemName
        this.itemSize = itemSize
        this.itemPosition = position
        this.qInp = qInPo
        setOnClick()
        setView()
        setContentView(binding.root)
    }

    fun setView() {
        binding.tvPosition.text = this.itemPosition
        binding.tvItemName.text = this.itemName
        binding.tvItemSize.text = this.itemSize

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
            binding.tvNum.text = inputN
        }

        binding.btnAddGoods.setOnClickListener {
            if (binding.tvNum.text.toString().toInt() != this.qInp.toInt()) {
                if (!check) {
                    AlertDialog.Builder(this@DoCheckQuantityActivity)
                        .setTitle("알림")
                        .setMessage("저장된 수량과 동일하지 않습니다.\n수량을 한번더 확인후 확인 버튼을 눌러주세요.")
                        .setPositiveButton(
                            "확인"
                        ) { p0, p1 -> check = true }
                        .create().show()
                } else {
                    AlertDialog.Builder(this@DoCheckQuantityActivity)
                        .setTitle("알림")
                        .setMessage("저장된 수량과 동일하지 않습니다.\n다음 작업으로 넘어가시겠습니까?")
                        .setPositiveButton(
                            "예"
                        ) { p0, p1 ->
                            intent.putExtra("q", binding.tvNum.text.toString())
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                        .setNegativeButton(
                            "아니요"
                        ) { p0, p1 -> check = true }
                        .create().show()
                }

            } else {
                intent.putExtra("q", binding.tvNum.text.toString())
                setResult(RESULT_OK, intent)
                finish()
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
    }

}