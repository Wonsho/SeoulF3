package com.example.seoulf3.outputwork.outputnondata.inputquantity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.setViewTreeOnBackPressedDispatcherOwner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.R
import com.example.seoulf3.databinding.ActivityInputQuantity2Binding
import com.example.seoulf3.outputwork.outputnondata.scanposition.ScanPositionActivity

class InputQuantityActivity : AppCompatActivity() {

    private lateinit var viewModel: InputQuantityViewModel
    private lateinit var binding: ActivityInputQuantity2Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::viewModel.isInitialized) {
            viewModel =
                ViewModelProvider(this@InputQuantityActivity)[InputQuantityViewModel::class.java]
        }

        if (!::binding.isInitialized) {
            binding = ActivityInputQuantity2Binding.inflate(layoutInflater)
        }


        val itemName = intent.getStringExtra("name").toString()
        val itemSize = intent.getStringExtra("size").toString()
        val itemCategoryCode = intent.getStringExtra("category").toString()
        val itemCode = intent.getStringExtra("itemCode").toString()
        val maxQ = intent.getStringExtra("quantity").toString()

        viewModel.setItemName(itemName)
        viewModel.setCategory(itemCategoryCode)
        viewModel.setItemSize(itemSize)
        viewModel.setItemCode(itemCode)
        viewModel.setMaxQ(maxQ)

        setView()
        setOnClick()
        setContentView(binding.root)
    }

    fun setView() {
        binding.tvItemName.text = viewModel.getItemName() + " "
        binding.tvItemSize.text = viewModel.getItemSize() + " "
        binding.tvMax.text = viewModel.getMaxQ()

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

            if (viewModel.getMaxQ().toInt() <= inputN.toInt()) {
                binding.tvNum.text = viewModel.getMaxQ()
                Toast.makeText(applicationContext, "최고 수량을 초과 하였습니다.",Toast.LENGTH_SHORT).show()
            } else {
                binding.tvNum.text = inputN

            }
        }

        binding.btnAddGoods.setOnClickListener {
            if (binding.tvNum.text == "0") {
                Toast.makeText(applicationContext, "숫자를 입력해주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val intent = Intent(this@InputQuantityActivity, ScanPositionActivity::class.java)
            intent.putExtra("name", viewModel.getItemName())
            intent.putExtra("size", viewModel.getItemSize())
            intent.putExtra("category", viewModel.getItemCategory())
            intent.putExtra("itemCode", viewModel.getItemCode())
            intent.putExtra("quantity", binding.tvNum.text.trim())
            startActivity(intent)
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