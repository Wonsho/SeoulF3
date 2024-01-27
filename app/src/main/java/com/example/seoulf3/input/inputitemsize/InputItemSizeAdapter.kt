package com.example.seoulf3.input.inputitemsize

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.seoulf3.databinding.InputsizelistBinding

class InputItemSizeAdapter : BaseAdapter() {
    private var sizeList = mutableListOf<String>()
    override fun getCount(): Int = this.sizeList.size

    override fun getItem(p0: Int): String = sizeList[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val binding = if (p1 == null) {
            InputsizelistBinding.inflate(LayoutInflater.from(p2!!.context))
        } else {
            InputsizelistBinding.bind(p1)
        }
        val itemSize = sizeList[p0]
        binding.tvItemSize.text = itemSize
        return binding.root
    }

    fun setSizeListData(sizeList: MutableList<String>) {
        this.sizeList = sizeList
    }
}