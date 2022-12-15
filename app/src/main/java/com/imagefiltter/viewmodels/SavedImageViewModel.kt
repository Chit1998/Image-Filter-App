package com.imagefiltter.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imagefiltter.repositories.SavedImageRepository
import com.imagefiltter.utilities.Coroutines
import java.io.File

class SavedImageViewModel(private val savedImageRepository: SavedImageRepository): ViewModel() {
    private val savedImageDataState = MutableLiveData<SavedImagesDataState>()
    val saveImagesUiState: LiveData<SavedImagesDataState> get() = savedImageDataState

    fun loadSavedImages(){
        Coroutines.io {
            runCatching {
                emitSavedImagesUiState(isLoading = true)
                savedImageRepository.loadSavedImages()
            }.onSuccess { savedImages ->
                if (!savedImages.isNullOrEmpty()){
                    emitSavedImagesUiState(savedImage = savedImages)
                }else{
                    emitSavedImagesUiState(error = "No image Found")
                }
            }.onFailure {
                emitSavedImagesUiState(error = it.message.toString())
            }
        }
    }

    private fun emitSavedImagesUiState (
        isLoading: Boolean = false,
        savedImage: List<Pair<File, Bitmap>>? = null,
        error: String? = null
    ){
        val dataState = SavedImagesDataState(isLoading, savedImage,error)
        savedImageDataState.postValue(dataState)
    }



    data class SavedImagesDataState(
        val isLoading: Boolean,
        val savedImage: List<Pair<File, Bitmap>>?,
        val error: String?
    )
}