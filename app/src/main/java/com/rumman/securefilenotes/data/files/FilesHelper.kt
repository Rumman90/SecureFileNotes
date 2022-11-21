package com.rumman.securefilenotes.data.files

import com.rumman.securefilenotes.data.models.FileModel
import java.util.LinkedList

interface FilesHelper {

    suspend fun getAllFiles() : Array<String?>
    suspend fun saveFile(fileName : String,fileContent : String) : Boolean
}