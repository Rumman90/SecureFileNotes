package com.rumman.securefilenotes.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.rumman.securefilenotes.data.models.FileModel
import java.text.SimpleDateFormat
import java.util.Date

fun Context.showToast(message : String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

@SuppressLint("SimpleDateFormat")
fun Context.getCurrentDate() : String{
    return SimpleDateFormat("ddMMyyyyhhmmss").format(Date())
}

fun <T : Any> ViewModel.convertModelToJsonString(data : T) : String {
    return Gson().toJson(data)
}

fun ViewModel.convertJsonToModelString(data : String?) : FileModel {
    return Gson().fromJson(data,FileModel::class.java)
}



