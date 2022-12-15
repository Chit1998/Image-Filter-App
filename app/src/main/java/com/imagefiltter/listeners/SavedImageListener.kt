package com.imagefiltter.listeners

import java.io.File

interface SavedImageListener {

    fun onImageClickListener(file: File)
}