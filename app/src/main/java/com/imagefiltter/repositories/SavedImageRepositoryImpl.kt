package com.imagefiltter.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File

class SavedImageRepositoryImpl(private val context: Context): SavedImageRepository {
    override suspend fun loadSavedImages(): List<Pair<File, Bitmap>>? {
        val savedImage = ArrayList<Pair<File, Bitmap>>()
        val dir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "save image"
        )

        dir.listFiles()?.let { data ->
            data.forEach { file ->
                savedImage.add(Pair(file, getPreviewBitmap(file)))
            }
            return savedImage
        } ?: return null
    }


    private fun getPreviewBitmap(file: File): Bitmap{
        val originalBitmap =    BitmapFactory.decodeFile(file.absolutePath)
        val width = originalBitmap.width
        val height = ((originalBitmap.height * width) / originalBitmap.width)

        return Bitmap.createScaledBitmap(originalBitmap, width, height, false)
    }
}