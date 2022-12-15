package com.imagefiltter.listeners

import com.imagefiltter.data.ImageFilter

interface ImageFilterListener {
    fun onFilterSelectedListener(imageFilter: ImageFilter)
}