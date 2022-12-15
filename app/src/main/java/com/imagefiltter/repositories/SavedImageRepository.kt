package com.imagefiltter.repositories

import android.graphics.Bitmap
import java.io.File

interface SavedImageRepository {
    suspend fun loadSavedImages(): List<Pair<File, Bitmap>>?
}