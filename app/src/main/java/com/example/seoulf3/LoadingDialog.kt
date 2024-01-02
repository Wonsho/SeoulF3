package com.example.seoulf3

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.example.seoulf3.databinding.LoadingDialogBinding

class LoadingDialog {
    fun getDialog(context: Context): AlertDialog {
        val diaBuilder = AlertDialog.Builder(context)
        val view = LoadingDialogBinding.inflate(LayoutInflater.from(context), null, false)
        diaBuilder.setView(view.root)
        diaBuilder.setCancelable(false)
        val dia = diaBuilder.create()
        dia.window!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        return dia
    }
}