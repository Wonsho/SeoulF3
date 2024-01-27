package com.example.seoulf3.outputupdate.updatesize.quantity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.seoulf3.R
import com.example.seoulf3.databinding.ActivityQuantityBinding
import com.example.seoulf3.outputupdate.updatesize.OutPutUpdateSizeActivity

class QuantityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuantityBinding
    private var itemName: String = ""
    private var itemSize: String = ""
    private var maxQ: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::binding.isInitialized) {
            binding = ActivityQuantityBinding.inflate(layoutInflater)
        }

        this.itemName = intent.getStringExtra("name").toString()
        this.itemSize = intent.getStringExtra("size").toString()
        this.maxQ = intent.getStringExtra("maxQ").toString()
        setContentView(binding.root)
        setOnClick()
        binding.tvItemName.text = itemName
        binding.tvItemSize.text = itemSize
        binding.tvMax.text = maxQ.toString()
    }

    private fun setOnClick() {

        fun inputN(inputN: String) {
            var text = binding.tvNum.text

            if (text == "0") {
                text = inputN
            } else {
                var q = text.toString()
                var inputN = q + inputN

                if (inputN.toInt() > maxQ.toInt()) {
                    text = maxQ
                    Toast.makeText(applicationContext, "재고 수량을 초과 하였습니다.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    text = inputN
                }
            }

            binding.tvNum.text = text
        }



        binding.c.setOnClickListener {
            var num = binding.tvNum.text
            if (num.length == 1) {
                binding.tvNum.text = "0"
            } else {
                val i = num.substring(0, num.length - 1)
                binding.tvNum.text = i
            }
        }
        binding.n0.setOnClickListener { inputN("0") }
        binding.n1.setOnClickListener { inputN("1") }
        binding.n2.setOnClickListener { inputN("2") }
        binding.n3.setOnClickListener { inputN("3") }
        binding.n4.setOnClickListener { inputN("4") }
        binding.n5.setOnClickListener { inputN("5") }
        binding.n6.setOnClickListener { inputN("6") }
        binding.n7.setOnClickListener { inputN("7") }
        binding.n8.setOnClickListener { inputN("8") }
        binding.n9.setOnClickListener { inputN("9") }
        binding.btnAddGoods.setOnClickListener {
            if (binding.tvNum.text == "0") {
                Toast.makeText(applicationContext, "입력 수량이 0 입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            setResult(RESULT_OK)
            val intent = Intent(this@QuantityActivity, OutPutUpdateSizeActivity::class.java)
            intent.putExtra("name", itemName)
            intent.putExtra("size", itemSize)
            intent.putExtra("quantity", binding.tvNum.text.trim().toString())
            setResult(RESULT_OK, intent)
            finish()
        }

    }
}