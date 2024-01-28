package com.example.seoulf3.outputwork.work.workoutput

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.LoadingDialog
import com.example.seoulf3.databinding.ActivityWorkOutBinding


//todo 자리 스캔 -> 품목명 알려주기, 수량 알려주기 (수량 부족 버튼) (확인)반복
class WorkOutActivity : AppCompatActivity() {
    private lateinit var viewModel: WorkOutViewModel
    private lateinit var dialog: AlertDialog
    private lateinit var binding: ActivityWorkOutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!::viewModel.isInitialized) {
            viewModel = ViewModelProvider(this)[WorkOutViewModel::class.java]
        }

        if (!::dialog.isInitialized) {
            dialog = LoadingDialog().getDialog(this@WorkOutActivity)
        }

        if (!::binding.isInitialized) {
            binding = ActivityWorkOutBinding.inflate(layoutInflater)
        }

        setContentView(binding.root)
    }
}