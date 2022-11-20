package com.rumman.securefilenotes.utils

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter

@BindingAdapter("android:text")
fun AppCompatTextView.AddNotes(value : String){
    text = value
}