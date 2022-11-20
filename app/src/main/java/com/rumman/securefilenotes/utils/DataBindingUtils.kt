package com.rumman.securefilenotes.utils

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter

/**
 * This binding adapter function is used to populate the recycler view item cells
 */
@BindingAdapter("android:text")
fun AppCompatTextView.AddNotes(value : String){
    text = value
}