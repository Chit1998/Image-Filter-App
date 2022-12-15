package com.imagefiltter.repositories

import android.graphics.Bitmap
import android.net.Uri
import com.imagefiltter.data.ImageFilter

interface EditImageRepository {

    suspend fun prepareImagePreview(imageUri: Uri): Bitmap?

    suspend fun getImageFilter(image: Bitmap): List<ImageFilter>

    suspend fun saveFilteredImage(filterBitmap: Bitmap): Uri?
}