package com.rumman.securefilenotes.data.files

import android.content.Context
import android.os.Build
import android.os.Environment
import com.rumman.securefilenotes.BuildConfig
import com.rumman.securefilenotes.data.models.FileModel
import com.rumman.securefilenotes.utils.convertJsonToModelString
import com.rumman.securefilenotes.utils.convertModelToJsonString
import com.rumman.securefilenotes.utils.getCurrentDate
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FileHelperImpl @Inject constructor(
    private val context : Context
): FilesHelper {

    private var outputStream : OutputStream? = null
    private var success : Boolean = false
    private var fileOutputStream : FileOutputStream? = null


    override suspend fun getAllFiles() : Array<String?> {
        val files = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                File("${context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)}/${BuildConfig.User_Password}").listFiles()
            } else {
                File("${Environment.getExternalStorageDirectory()}/${BuildConfig.User_Password}").listFiles()
            }
            val fileNames = arrayOfNulls<String>(files?.size ?: 0)
            files?.mapIndexed { index, item ->
                fileNames[index] = item?.readText()
            }
        return fileNames

    }

    override suspend fun saveFile(fileName : String,fileContent : String) : Boolean{
        try {
            val path = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                "${context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)}/${BuildConfig.User_Password}"
            }else{
                "${Environment.getExternalStorageDirectory()}/${BuildConfig.User_Password}"
            }
            val file = File(path)
            file.mkdirs()
            val outputFile = File(file,fileName)
            if(outputFile.exists()){
                outputFile.delete()
            }
            fileOutputStream = FileOutputStream(outputFile)
            fileOutputStream!!.write(fileContent.toByteArray())
            outputStream = BufferedOutputStream(
                fileOutputStream
            )
            success = true
        }catch (e : Exception){
            success = false
        }finally {
            if(outputStream != null){
                outputStream!!.flush()
                outputStream!!.close()
            }
            if(fileOutputStream != null){
                fileOutputStream!!.close()
            }
        }

       return success

    }
}