package com.example.seoulf3.outputwork

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.seoulf3.databinding.OutputworkdatelistviewBinding
import java.text.SimpleDateFormat

class OutputWorkAdapter(val callBack: OutPutWorkActivity.DeleteCallBack) : BaseAdapter() {
    private var dateList = mutableListOf<String>()
    override fun getCount(): Int = dateList.size

    override fun getItem(p0: Int): String = dateList[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val binding: OutputworkdatelistviewBinding = if (p1 == null) {
            OutputworkdatelistviewBinding.inflate(LayoutInflater.from(p2!!.context))
        } else {
            OutputworkdatelistviewBinding.bind(p1)
        }

        val date = this.dateList[p0]
        val format =  SimpleDateFormat("yyyy-MM-dd hh:mm")
        binding.tvDate.text = format.format(date.toLong())
        binding.btnDelete.setOnClickListener {
            //todo callBack
            callBack.callBack(p0)
        }
        return binding.root
    }

    fun setData(data: MutableList<String>) {
        this.dateList = data
    }
}