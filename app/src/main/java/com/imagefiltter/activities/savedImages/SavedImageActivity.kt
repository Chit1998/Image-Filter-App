package com.imagefiltter.activities.savedImages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import com.imagefiltter.activities.editimage.EditImageActivity
import com.imagefiltter.activities.filteredImages.FilteredImagesActivity
import com.imagefiltter.adapters.SavedImagesAdapter
import com.imagefiltter.databinding.ActivitySavedImageBinding
import com.imagefiltter.listeners.SavedImageListener
import com.imagefiltter.utilities.displayToast
import com.imagefiltter.viewmodels.SavedImageViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class SavedImageActivity : AppCompatActivity(), SavedImageListener {

    private lateinit var binding: ActivitySavedImageBinding
    private val viewModel: SavedImageViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setObserver()
        setListener()
        viewModel.loadSavedImages()
    }


    private fun setObserver(){
        viewModel.saveImagesUiState.observe(this){
            val saveImagesDataState = it ?: return@observe
            binding.savedImageProgressBar.visibility =
                if (saveImagesDataState.isLoading) View.VISIBLE else View.GONE
            saveImagesDataState.savedImage?.let { savedImages ->
                displayToast("${savedImages.size} image loaded")
                SavedImagesAdapter(savedImages, this).also { adapter ->
                    with(binding.savedImagesRecyclerView){
                        this.adapter = adapter
                        visibility = View.VISIBLE
                    }
                }
            } ?: run {
                saveImagesDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
    }

    private fun setListener(){
        binding.imageBack.setOnClickListener {
            @Suppress("DEPRECATION")
            onBackPressed()
        }
    }

    override fun onImageClickListener(file: File) {
        val fileUri = FileProvider.getUriForFile(
            applicationContext,
            "${packageName}.provider",
            file
        )
        Intent(applicationContext, FilteredImagesActivity::class.java)
            .also { filterImageIntent ->
                filterImageIntent.putExtra(EditImageActivity.KEY_FILTERED_IMAGE_URI, fileUri)
                startActivity(filterImageIntent)
            }
    }
}