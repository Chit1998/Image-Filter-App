package com.imagefiltter.activities.filteredImages

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.imagefiltter.R
import com.imagefiltter.activities.editimage.EditImageActivity
import com.imagefiltter.databinding.ActivityFilteredImagesBinding

class FilteredImagesActivity : AppCompatActivity() {

    private lateinit var fileUri: Uri
    private lateinit var binding: ActivityFilteredImagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilteredImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        displayFilteredImages()
        setListener()
    }

    private fun displayFilteredImages(){
        @Suppress("DEPRECATION")
        intent.getParcelableExtra<Uri>(EditImageActivity.KEY_FILTERED_IMAGE_URI)?.let { imageUri ->
            fileUri = imageUri
            binding.imageFilteredImage.setImageURI(imageUri)
        }
    }

    private fun setListener(){
        binding.fabShare.setOnClickListener{
            with(Intent(Intent.ACTION_SEND)){
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                type = "image/*"
                startActivity(this)
            }
        }

        binding.imageBack.setOnClickListener {
            @Suppress("DEPRECATION")
            onBackPressed()
        }
    }
}