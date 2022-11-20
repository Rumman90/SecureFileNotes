package com.rumman.securefilenotes.utils

/**
 * Repository Response Validation either success,error and body
 */
sealed class Resources<out T>{
    data class Success<T>(val data : T) : Resources<T>()
    data class Error(val error : String) : Resources<Nothing>()
    object Loading : Resources<Nothing>()
}
