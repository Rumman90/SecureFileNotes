package com.rumman.securefilenotes.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.rumman.securefilenotes.BuildConfig
import com.rumman.securefilenotes.data.models.FileModel
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * This extension function is used for showing toast
 */
fun Context.showToast(message : String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

/**
 * This extension function will return the current date
 */
@SuppressLint("SimpleDateFormat")
fun ViewModel.getCurrentDate() : String{
    return SimpleDateFormat("ddMMyyyyhhmmss").format(Date())
}

/**
 * This extension function convert data model to json string
 */
fun <T : Any> ViewModel.convertModelToJsonString(data : T) : String {
    return Gson().toJson(data)
}

/**
 * This extension function convert json String to data model
 */
fun ViewModel.convertJsonToModelString(data : String?) : FileModel {
    return Gson().fromJson(data,FileModel::class.java)
}

/**
 * This extension function generate key according to user login password
 */
fun ViewModel.generateKey() : SecretKeySpec {
    val digest = MessageDigest.getInstance("SHA-256")
    val bytes = BuildConfig.User_Password.toByteArray()
    digest.update(bytes,0,bytes.size)
    val key = digest.digest()
    return SecretKeySpec(key,0,16,"AES")
}

/**
 * This extension function is used to encrypt the content
 */
fun ViewModel.encrypt(content : String) : ByteArray {
    val plainText = content.toByteArray(Charsets.UTF_8)
    val key = generateKey()
    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    cipher.init(Cipher.ENCRYPT_MODE, key)
    return cipher.doFinal(plainText)
}

/**
 * This extension function is used to decrypt the content
 */
fun ViewModel.decrypt(content : ByteArray) : String {
    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    val key = generateKey()
    cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(getEncryptedCipherIvValue(key)))
    val cipherText = cipher.doFinal(content)
    return buildString(cipherText)
}

/**
 * This function returns the encrypted cipher iv value for decryption
 */
private fun getEncryptedCipherIvValue(key : SecretKeySpec) : ByteArray{
    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    cipher.init(Cipher.ENCRYPT_MODE,key)
    return cipher.iv
}

private fun buildString(text: ByteArray): String{
    val sb = StringBuilder()
    for (char in text) {
        sb.append(char.toInt().toChar())
    }
    return sb.toString()
}



