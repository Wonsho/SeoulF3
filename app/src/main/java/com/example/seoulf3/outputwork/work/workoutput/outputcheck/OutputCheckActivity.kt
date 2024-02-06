package com.example.seoulf3.outputwork.work.workoutput.outputcheck

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.setViewTreeOnBackPressedDispatcherOwner
import androidx.appcompat.app.AlertDialog
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
        val position = intent.getStringExtra("position").toString()
        val itemName = intent.getStringExtra("name").toString()
        val itemSize = intent.getStringExtra("size").toString()
        val remainedBox = intent.getStringExtra("releaseQ").toString()
        val maxQ = intent.getStringExtra("positionQ").toString()

        binding.tvPosition.text = position
        binding.tvItemSize.text = itemSize
        binding.tvItemName.text = itemName
        binding.tvQuantity.text = remainedBox
        binding.tvMaxNum.text = if(remainedBox.toInt() >= maxQ.toInt()) (
            maxQ
        ) else {
            remainedBox
        }
    }

    fun setOnClick() {

        fun onClickNum(n: String) {
            var num = binding.tvNum.text.toString()

            if (num.toInt() == binding.tvQuantity.text.toString().toInt()) {
                return
            }

            if (num.toInt() == binding.tvMaxNum.text.toString().toInt()) {
                return
            }


            if (num != "0") {
                num += n

                binding.tvNum.text = num

                if (num.toInt() > binding.tvQuantity.text.toString().toInt() || num.toInt() > binding.tvMaxNum.text.toString().toInt()) {
                    binding.tvNum.text = binding.tvMaxNum.text.toString()
                    Toast.makeText(applicationContext, "수량 초과되었습니다.", Toast.LENGTH_SHORT).show()
                    return
                }



            } else {

                if (n == "0") {
                    return
                } else {
                    binding.tvNum.text = n

                    if (n.toInt() > binding.tvQuantity.text.toString().toInt() || n.toInt() > binding.tvMaxNum.text.toString().toInt()) {
                        binding.tvNum.text = binding.tvMaxNum.text.toString()
                        Toast.makeText(applicationContext, "수량 초과되었습니다.", Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }
        }

        binding.btnAddGoods.setOnClickListener {

            if(binding.tvNum.text.toString() != binding.tvMaxNum.text.toString()) {
                val dialog = AlertDialog.Builder(this@OutputCheckActivity)
                    .setTitle("알림")
                    .setMessage("출고가능 숫자보다 적습니다.\n다음작업을 진행할까요?")
                    .setPositiveButton("예", object : DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            //todo 다음 작업 진행 -> 만약 한개일 경우 계속 보여주기
                        }

                    })
                    .setNegativeButton("아니요", object : DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                        }

                    })
                    .setNeutralButton("재고 이상", object : DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            //todo 에러 등록
                        }
                    })
                dialog.create().show()
            } else {
                //todo 다음 작업 진행
            }
            //todo 만약 maxQ와 수량이 맞지 않으면 알람 띄우기

        }
        binding.c.setOnClickListener {
            var num = binding.tvNum.text.toString()

            if (num.length == 1) {
                num = "0"
            } else {
                num = num.substring(0, num.length-1)
            }

            binding.tvNum.text = num
        }
        binding.btnError.setOnClickListener {
            val dialog = AlertDialog.Builder(this@OutputCheckActivity)
                .setTitle("수량 오류")
                .setMessage("현 위치의 수량이 맞지 않습니까?")
                .setPositiveButton("예", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {

                    }

                })
                .setNegativeButton("아니요", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {

                    }
                })
            dialog.create().show()
        }
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