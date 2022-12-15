package com.imagefiltter.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imagefiltter.data.ImageFilter
import com.imagefiltter.repositories.EditImageRepository
import com.imagefiltter.utilities.Coroutines

class EditImageViewModel(private val editImageRepository: EditImageRepository): ViewModel() {

//    region:: Prepare image preview
    private val imagePreviewDataState = MutableLiveData<ImagePreviewDataState>()
    val imagePreviewUiState: LiveData<ImagePreviewDataState> get() = imagePreviewDataState

    fun prepareImagePreview(imageUri: Uri){
        Coroutines.io {
            runCatching {
                emitImagePreviewUiState(isLoading = true)
                editImageRepository.prepareImagePreview(imageUri)
            }.onSuccess { bitmap ->
                if (bitmap != null){
                    emitImagePreviewUiState(bitmap = bitmap)
                }else{
                    emitImagePreviewUiState(error = "Unable to prepare image preview")
                }
            }.onFailure {
                emitImagePreviewUiState(error = it.message.toString())
            }
        }
    }

    private fun emitImagePreviewUiState(
        isLoading: Boolean = false,
        bitmap: Bitmap? = null,
        error: String? = null
    ){
        val dataState = ImagePreviewDataState(isLoading,bitmap,error)
        imagePreviewDataState.postValue(dataState)
    }
    data class ImagePreviewDataState(
        val isLoading: Boolean,
        val bitmap: Bitmap?,
        val error: String?
    )
//    endregion

//    region:: load image filters

    private val imageFiltersDataState = MutableLiveData<ImageFilterDataState>()
    val imageFilterUiState: LiveData<ImageFilterDataState> get() = imageFiltersDataState

    fun loadImageFilter(originalImage: Bitmap){
        Coroutines.io {
            runCatching {
                emitImageFilterUiState(isLoading = true)
                editImageRepository.getImageFilter(getPreviewImage(originalImage))
            }.onSuccess { imageFilters ->
                emitImageFilterUiState(imageFilters = imageFilters)
            }.onFailure {
                emitImageFilterUiState(error = it.message.toString())
            }
        }
    }

//    Image width * height

    private fun getPreviewImage(originalImage: Bitmap):Bitmap {
        return runCatching {
            val previewWidth = originalImage.width
            val previewHeight = originalImage.height * previewWidth / originalImage.width
            Bitmap.createScaledBitmap(originalImage,previewWidth,previewHeight,false)
        }.getOrDefault(originalImage)
    }

    private fun emitImageFilterUiState(
        isLoading: Boolean = false,
        imageFilters: List<ImageFilter>? = null,
        error: String? = null
    ){
        val dataState = ImageFilterDataState(isLoading,imageFilters,error)
        imageFiltersDataState.postValue(dataState)
    }

    data class ImageFilterDataState(
    val isLoading: Boolean,
    val imageFilters: List<ImageFilter>?,
    val  error: String?
    )
//    endregion

    //region:: Save filtered image

    private val saveFilteredImageDataState = MutableLiveData<SaveFilteredImageDataState>()
    val saveFilteredImageUiState: LiveData<SaveFilteredImageDataState> get() = saveFilteredImageDataState

    fun saveFilteredImage(filteredBitmap: Bitmap){
        Coroutines.io {
            runCatching {
                emitSaveFilteredImageUiState(isLoading = true)
                editImageRepository.saveFilteredImage(getPreviewImage(filteredBitmap))
            }.onSuccess { saveImageUri ->
                emitSaveFilteredImageUiState(uri = saveImageUri)
            }.onFailure {
                emitSaveFilteredImageUiState(error = it.message.toString())
            }
        }
    }

    private fun emitSaveFilteredImageUiState(
        isLoading: Boolean = false,
        uri: Uri? = null,
        error: String? = null
    ){
        val dataState = SaveFilteredImageDataState(isLoading,uri,error)
        saveFilteredImageDataState.postValue(dataState)
    }

    data class SaveFilteredImageDataState(
        val isLoading: Boolean,
        val uri: Uri?,
        val error: String?
    )
    //endregion

}