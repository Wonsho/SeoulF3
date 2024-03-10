package com.example.seoulf3.outputwork.work.workoutput.outputcheck

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.databinding.ActivityOutputCheckBinding
import com.example.seoulf3.outputwork.outputnondata.scanposition.ScanPositionActivity

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
        setOnClick()
        setIntentData()
        setView()
    }

    private fun setView() {
        binding.tvPosition.text = viewModel.getPosition()
        binding.tvItemName.text = viewModel.getItemName()
        binding.tvItemSize.text = viewModel.getItemSize()
        binding.tvQuantity.text = viewModel.getItemNeedQ()
        binding.tvMaxNum.text = viewModel.getItemSavedQ()
    }

    private fun getIntentData(key: String): String {
        return intent.getStringExtra(key)!!
    }


    private fun setIntentData() {
        viewModel.setItemName(getIntentData("itemName"))
        viewModel.setPosition(getIntentData("position"))
        viewModel.setItemSize(getIntentData("itemSize"))
        viewModel.setItemNeedQ(getIntentData("needQ"))
        viewModel.setItemSavedQ(getIntentData("positionSavedQ"))
    }


    fun showErrorDialog() {
        AlertDialog.Builder(this@OutputCheckActivity)
            .setTitle("알림")
            .setMessage("현재 품목이 존재하지 않습니까?")
            .setPositiveButton("예") { p0, p1 ->
                //에러 입력
                intentOnlyError()
            }
            .setNegativeButton("아니요") { p0, p1 ->
                AlertDialog.Builder(this@OutputCheckActivity)
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
        AlertDialog.Builder(this@OutputCheckActivity)
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

        binding.c.setOnClickListener {
            var num = binding.tvNum.text.toString()

            if (num.length == 1) {
                num = "0"
            } else {
                num = num.substring(0, num.length-1)
            }

            binding.tvNum.text = num
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
        setResult(result, intent)
        finish()
    }

    fun intentNowEnough() {
        //충분치 않는 데이터
        val result = this.result.DATA_NOT_ENOUGH
        val q = binding.tvNum.text.toString()
        intent.putExtra("q", q)
        setResult(result, intent)
        finish()
    }

    fun intentWithError() {
        //에러와 함께 충분치 않은 데이터
        val result = this.result.DATA_WITH_ERROR
        val q = binding.tvNum.text.toString()
        intent.putExtra("q", q)
        setResult(result, intent)
        finish()
    }

    fun intentOnlyError() {
        //오직 에러만 보내기
        val result = this.result.ONLY_ERROR
        val q = binding.tvNum.text.toString()
        intent.putExtra("q", q)
        setResult(result, intent)
        finish()
    }

    fun showDialog() {
        AlertDialog.Builder(this@OutputCheckActivity)
            .setTitle("알림")
            .setMessage("모든 작업을 중지하고 뒤로가시겠습니까?")
            .setPositiveButton("예") { p0, p1 ->
                setResult(RESULT_CANCELED)
                finish()
            }
            .setNegativeButton("아니요") { p0, p1 -> }
            .create().show()

    }

    override fun onBackPressed() {
        showDialog()
    }
}