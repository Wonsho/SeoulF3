package com.example.seoulf3.input.inputquantity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.seoulf3.R
import com.example.seoulf3.databinding.ActivityInputQuantityBinding

class InputQuantityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputQuantityBinding
    private lateinit var position: String
    private lateinit var itemName: String
    private lateinit var itemSize: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::binding.isInitialized) {
            binding = ActivityInputQuantityBinding.inflate(layoutInflater)
        }

        setOnClick()
        setContentView(binding.root)
        setView()
    }

    fun setView() {
        position = intent.getStringExtra("position").toString()
        itemName = intent.getStringExtra("name").toString()
        itemSize = intent.getStringExtra("size").toString()

        binding.tvItemName.text = " " + itemName + " "
        binding.tvPosition.text = " " + position + " "
        binding.tvItemSize.text = " " + itemSize + " "
    }

    fun setOnClick() {
        binding.btnRescan.setOnClickListener {
            setResult(RESULT_FIRST_USER)
            finish()
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
            binding.tvNum.text = inputN
        }

        binding.btnAddGoods.setOnClickListener {
            if (binding.tvNum.text == "0") {
                Toast.makeText(applicationContext, "숫자를 입력해주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            setResult(RESULT_OK, intent.putExtra("quantity", binding.tvNum.text.toString()))
            finish()
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
        binding.n0.setOnClickListener {inputNum("0") }
        binding.n1.setOnClickListener {inputNum("1") }
        binding.n2.setOnClickListener {inputNum("2") }
        binding.n3.setOnClickListener {inputNum("3") }
        binding.n4.setOnClickListener {inputNum("4") }
        binding.n5.setOnClickListener {inputNum("5") }
        binding.n6.setOnClickListener {inputNum("6") }
        binding.n7.setOnClickListener {inputNum("7") }
        binding.n8.setOnClickListener {inputNum("8") }
        binding.n9.setOnClickListener {inputNum("9") }
    }
}