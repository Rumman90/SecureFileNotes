package com.rumman.securefilenotes.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.rumman.securefilenotes.BuildConfig
import com.rumman.securefilenotes.data.models.FileModel
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.text.SimpleDateFormat
import java.util.Date
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
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
 * This extension function is used to encrypt the content
 */
@Throws(
    NoSuchPaddingException::class,NoSuchAlgorithmException::class,
    InvalidAlgorithmParameterException::class,
    InvalidKeyException::class,
    BadPaddingException::class,
    IllegalBlockSizeException::class)
fun ViewModel.encrypt(content : String, password : String, salt : String) : String {
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, getKeyFromPassword(password,salt),IvParameterSpec(byteArrayOf(1,3,5,7,9,11,13,15,21,25,41,55,68,73,80,95)))
    val cipherText = cipher.doFinal(content.toByteArray())
    return android.util.Base64.encodeToString(cipherText,android.util.Base64.DEFAULT)
}

/**
 * This extension function is used to decrypt the content
 */
@Throws(NoSuchPaddingException::class,NoSuchAlgorithmException::class,InvalidAlgorithmParameterException::class,InvalidKeyException::class,BadPaddingException::class,IllegalBlockSizeException::class)
fun ViewModel.decrypt(content : String,password : String,salt : String) : String {
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassword(password,salt),IvParameterSpec(byteArrayOf(1,3,5,7,9,11,13,15,21,25,41,55,68,73,80,95)))
    val plainText = cipher.doFinal(android.util.Base64.decode(content,android.util.Base64.DEFAULT))
    return String(plainText)
}

@Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
private fun getKeyFromPassword(password: String,salt: String) : SecretKey {
    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    val spec = PBEKeySpec(password.toCharArray(),salt.toByteArray(),65536,256)
    return SecretKeySpec(factory.generateSecret(spec).encoded,"AES")
}



