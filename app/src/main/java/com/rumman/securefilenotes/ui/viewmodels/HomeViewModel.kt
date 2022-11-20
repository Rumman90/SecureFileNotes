package com.rumman.securefilenotes.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rumman.securefilenotes.data.files.FilesHelper
import com.rumman.securefilenotes.data.models.FileModel
import com.rumman.securefilenotes.utils.Resources
import com.rumman.securefilenotes.utils.convertJsonToModelString
import com.rumman.securefilenotes.utils.convertModelToJsonString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.LinkedList
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val filesHelper: FilesHelper
): ViewModel() {

    private val _notesList = LinkedList<FileModel>()

    private val _notesObserver = MutableLiveData<Resources<String>>()
        val notesObserver get() = _notesObserver

    private val _fileObserver = MutableLiveData<Resources<List<FileModel>>>()
        val fileObserver get() = _fileObserver

    fun addNotes(title : String,notes : String){
        _notesObserver.postValue(Resources.Loading)
        viewModelScope.launch {
            val isRecordSaved = filesHelper.saveFile(convertModelToJsonString(FileModel(title,notes)))
            if(isRecordSaved){
                _notesObserver.postValue(Resources.Success("Success"))
            }else{
                _notesObserver.postValue(Resources.Error("Error"))
            }

        }
    }

    fun getFiles(){
        _fileObserver.postValue(Resources.Loading)
        viewModelScope.launch {
            val allRecords = filesHelper.getAllFiles()
            if(allRecords.isNotEmpty()){
                _notesList.clear()
                allRecords.forEach {
                    _notesList.add(convertJsonToModelString(it))
                }
                _fileObserver.postValue(Resources.Success(_notesList))
            }else{
                _fileObserver.postValue(Resources.Error("No Notes Found"))
            }

        }
    }

}