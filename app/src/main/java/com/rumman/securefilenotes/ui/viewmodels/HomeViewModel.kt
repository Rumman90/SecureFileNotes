package com.rumman.securefilenotes.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rumman.securefilenotes.BuildConfig
import com.rumman.securefilenotes.data.files.FilesHelper
import com.rumman.securefilenotes.data.models.FileModel
import com.rumman.securefilenotes.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.LinkedList
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val filesHelper: FilesHelper
): ViewModel() {

    private val viewModelJob = SupervisorJob()

    private val ioScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    private val _notesList = LinkedList<FileModel>()

    private val _notesObserver = MutableLiveData<Resources<String>>()
        val notesObserver get() = _notesObserver

    private val _fileObserver = MutableLiveData<Resources<List<FileModel>>>()
        val fileObserver get() = _fileObserver

    /**
     * The below function calls file helper save file method
     */
    fun addNotes(title : String,notes : String){
        _notesObserver.postValue(Resources.Loading)
        ioScope.launch {
                val isRecordSaved = filesHelper.saveFile(getCurrentDate()+".txt",encrypt(convertModelToJsonString(FileModel(title,notes)),BuildConfig.User_Password,BuildConfig.User_Salt))
                if(isRecordSaved){
                    _notesObserver.postValue(Resources.Success("Success"))
                }else{
                    _notesObserver.postValue(Resources.Error("Error"))
                }
        }

    }

    /**
     * The below function will fetch all files
     */
    fun getFiles(){
        _fileObserver.postValue(Resources.Loading)
        ioScope.launch {
                val allRecords = filesHelper.getAllFiles()
                if(allRecords.isNotEmpty()){
                    _notesList.clear()
                    allRecords.forEach {
                    _notesList.add(convertJsonToModelString(
                        it
                        ?.let { it1 -> decrypt(it1,BuildConfig.User_Password,BuildConfig.User_Salt)}))
                    }
                    _fileObserver.postValue(Resources.Success(_notesList))
                }else{
                    _fileObserver.postValue(Resources.Error("No Notes Found"))
                }
            }

    }

    fun clearNotesObserver(){
        _notesObserver.postValue(null)
    }

    fun clearFileObserver(){
        _fileObserver.postValue(null)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}