package com.example.seoulf3.input.inputitemsize

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.seoulf3.databinding.InputSizeListBinding

class InputItemSizeAdapter : BaseAdapter() {
    private var sizeList = mutableListOf<String>()
    override fun getCount(): Int = sizeList.size

    override fun getItem(p0: Int): String = sizeList[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val binding = if (p1 == null) {
            InputSizeListBinding.inflate(LayoutInflater.from(p2!!.context))
        } else {
            InputSizeListBinding.bind(p1)
        }

        val item = sizeList[p0]
        binding.tvItemSize.text = item
        return binding.root
    }

    fun setList(list: MutableList<String>) {
        this.sizeList = list
    }
}