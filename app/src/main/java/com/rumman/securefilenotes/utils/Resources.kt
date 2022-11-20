package com.rumman.securefilenotes.utils

sealed class Resources<out T>{
    data class Success<T>(val data : T) : Resources<T>()
    data class Error(val error : String) : Resources<Nothing>()
    object Loading : Resources<Nothing>()
}
