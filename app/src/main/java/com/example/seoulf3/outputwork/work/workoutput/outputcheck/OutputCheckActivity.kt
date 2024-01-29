package com.example.seoulf3.outputwork.work.workoutput.outputcheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.setViewTreeOnBackPressedDispatcherOwner
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.R
import com.example.seoulf3.databinding.ActivityOutputCheckBinding
import kotlin.math.max

class OutputCheckActivity : AppCompatActivity() {
    private lateinit var viewModel: OutputCheckViewModel
    private lateinit var binding: ActivityOutputCheckBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::viewModel.isInitialized) {
            viewModel =
                ViewModelProvider(this@OutputCheckActivity)[OutputCheckViewModel::class.java]
        }

        if (!::binding.isInitialized) {
            binding = ActivityOutputCheckBinding.inflate(layoutInflater)
        }
        setContentView(binding.root)
        setView()
        setOnClick()
    }


    fun setView() {
        val position = ""
        val itemName = ""
        val itemSize = ""
        val remainedBox = ""
        val maxQ = ""

        binding.tvPosition.text = position
        binding.tvItemSize.text = itemSize
        binding.tvItemName.text = itemName
        binding.tvQuantity.text = remainedBox
        binding.tvMaxNum.text = maxQ
    }

    fun setOnClick() {

        fun onClickNum(n: String) {
            var num = binding.tvNum.text.toString()

            if (num != "0") {
                num += n
                binding.tvNum.text = num
            } else {

                if (n == "0") {
                    return
                } else {
                    binding.tvNum.text = n
                }
            }
        }

        binding.btnAddGoods.setOnClickListener { }
        binding.c.setOnClickListener { }
        binding.n0.setOnClickListener { onClickNum("0") }
        binding.n1.setOnClickListener { onClickNum("1") }
        binding.n2.setOnClickListener { onClickNum("2") }
        binding.n3.setOnClickListener { onClickNum("3") }
        binding.n4.setOnClickListener { onClickNum("4") }
        binding.n5.setOnClickListener { onClickNum("5") }
        binding.n6.setOnClickListener { onClickNum("6") }
        binding.n7.setOnClickListener { onClickNum("7") }
        binding.n8.setOnClickListener { onClickNum("8") }
        binding.n9.setOnClickListener { onClickNum("9") }
    }

}