package com.imagefiltter.activities.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.imagefiltter.activities.editimage.EditImageActivity
import com.imagefiltter.activities.savedImages.SavedImageActivity
import com.imagefiltter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListener()
    }

    private fun setListener(){
        binding.btnViewEditImage.setOnClickListener{
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ).also { packerIntent->
                packerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                @Suppress("DEPRECATION")
                startActivityForResult(packerIntent, REQUEST_CODE_PICK_IMAGE)
            }
        }

        binding.btnViewSaveImages.setOnClickListener{
            Intent(applicationContext, SavedImageActivity::class.java)
                .also {
                    startActivity(it)
                }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK){
            data?.data?.let { imageUri ->
                Intent(applicationContext, EditImageActivity::class.java)
                    .also { editImageIntent ->
                        editImageIntent.putExtra(KEY_IMAGE_URI, imageUri)
                        startActivity(editImageIntent)
                    }
            }
        }
    }

    companion object{
        private const val REQUEST_CODE_PICK_IMAGE = 1
        const val KEY_IMAGE_URI = "imageUri"
    }
}