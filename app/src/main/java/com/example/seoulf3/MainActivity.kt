package com.example.seoulf3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seoulf3.checkstock.BeforeCheckStockActivity

import com.example.seoulf3.databinding.ActivityMainBinding
import com.example.seoulf3.input.InputItemNameListActivity
import com.example.seoulf3.outputupdate.OutPutUpdateActivity
import com.example.seoulf3.outputwork.OutPutWorkActivity
import com.example.seoulf3.searchstock.SearchStockActivity

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        }

        if (!::binding.isInitialized) {
            binding = ActivityMainBinding.inflate(layoutInflater)
        }
        setContentView(binding.root)
        setOnClick()
        viewModel.buildDatabase()

    }

    private fun setOnClick() {
        binding.btnCheckStock.setOnClickListener {
            val intent = Intent(this@MainActivity, BeforeCheckStockActivity::class.java)
            startActivity(intent)
        }
        binding.btnInput.setOnClickListener {
            val intent = Intent(this@MainActivity, InputItemNameListActivity::class.java)
            startActivity(intent)
        }

        binding.btnOutputupdate.setOnClickListener {
            val intent = Intent(this@MainActivity, OutPutUpdateActivity::class.java)
            startActivity(intent)
        }

        binding.btnSearchStock.setOnClickListener {
            //todo 재고 확인
            val intent = Intent(this@MainActivity, SearchStockActivity::class.java)
            startActivity(intent)
        }

        binding.btnOutput.setOnClickListener {
            val intent = Intent(this@MainActivity, OutPutWorkActivity::class.java)
            startActivity(intent)
        }
    }
}