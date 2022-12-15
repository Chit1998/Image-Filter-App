package com.imagefiltter.activities.editimage

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.imagefiltter.activities.filteredImages.FilteredImagesActivity
import com.imagefiltter.adapters.ImageFilterAdapter
import com.imagefiltter.data.ImageFilter
import com.imagefiltter.activities.main.MainActivity
import com.imagefiltter.databinding.ActivityEditImageBinding
import com.imagefiltter.listeners.ImageFilterListener
import com.imagefiltter.utilities.displayToast
import com.imagefiltter.utilities.show
import com.imagefiltter.viewmodels.EditImageViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImageActivity : AppCompatActivity(), ImageFilterListener {

    companion object{
        const val KEY_FILTERED_IMAGE_URI = "filteredImageUri"
    }

    private lateinit var binding: ActivityEditImageBinding

    private val viewModel:EditImageViewModel by viewModel()

    private lateinit var gpuImage: GPUImage

//    Image Bitmaps
    private lateinit var originalBitmap: Bitmap
    private val filterBitmap = MutableLiveData<Bitmap>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListener()
//        displayImagePreview()
        setupObservers()
        prepareImagePreview()
    }

    private fun setupObservers(){
        viewModel.imagePreviewUiState.observe(this) {
            val dataState = it ?: return@observe
            binding.previewProgressBar.visibility =
                if (dataState.isLoading) View.VISIBLE else View.GONE
            dataState.bitmap?.let { bitmap ->

//                for the first time 'filter image = original image'
                originalBitmap = bitmap
                filterBitmap.value = bitmap
                with(originalBitmap){
                    gpuImage.setImage(this)
                    binding.imagePreview.show()
                    viewModel.loadImageFilter(this)
                }
//                binding.imagePreview.setImageBitmap(bitmap)

            }?: kotlin.run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
        viewModel.imageFilterUiState.observe(this) {
            val imageFilterDataState = it ?: return@observe
            binding.imageFilterProgressBar.visibility =
                if (imageFilterDataState.isLoading) View.VISIBLE else View.GONE
            imageFilterDataState.imageFilters?.let { imageFilters ->
                ImageFilterAdapter(imageFilters, this).also { adapter ->
                    binding.filterRecyclerView.adapter = adapter
                }
            } ?: kotlin.run {
                imageFilterDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }

        filterBitmap.observe(this) { bitmap ->
            binding.imagePreview.setImageBitmap(bitmap)

        }

        viewModel.saveFilteredImageUiState.observe(this) {
            val saveFilteredImageDataState = it ?: return@observe
            if (saveFilteredImageDataState.isLoading){
                binding.imageSave.visibility = View.GONE
                binding.savingProgressBar.visibility = View.VISIBLE
            }else{
                binding.imageSave.visibility = View.VISIBLE
                binding.savingProgressBar.visibility = View.GONE
            }

            saveFilteredImageDataState.uri?.let { saveFilteredUri ->
                Intent(
                    applicationContext,
                    FilteredImagesActivity::class.java
                ).also { filterImageIntent ->
                    filterImageIntent.putExtra(KEY_FILTERED_IMAGE_URI,saveFilteredUri)
                    startActivity(filterImageIntent)
                }
            } ?: kotlin.run {
                saveFilteredImageDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
    }

    private fun prepareImagePreview(){
        gpuImage = GPUImage(applicationContext)
        @Suppress("DEPRECATION")
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)
        }
    }

//    private fun displayImagePreview(){
//        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
//            val inputStream = contentResolver.openInputStream(imageUri)
//            val bitmap = BitmapFactory.decodeStream(inputStream)
//            binding.imagePreview.setImageBitmap(bitmap)
//            binding.imagePreview.visibility = View.VISIBLE
//        }
//    }

    private fun setListener(){
        binding.imageBack.setOnClickListener{
            @Suppress("DEPRECATION")
            onBackPressed()
        }

        binding.imageSave.setOnClickListener{
            filterBitmap.value?.let { bitmap ->  
                viewModel.saveFilteredImage(bitmap)
            }
        }

        /**
                This will show original image when we long click the imageView until we release click
                so that we can see difference between original image and filtered image
         */

        binding.imagePreview.setOnLongClickListener{
            binding.imagePreview.setImageBitmap(originalBitmap)
            return@setOnLongClickListener false
        }

        binding.imagePreview.setOnClickListener{
            binding.imagePreview.setImageBitmap(filterBitmap.value)
        }
    }

    override fun onFilterSelectedListener(imageFilter: ImageFilter) {
        with(imageFilter){
            with(gpuImage){
                setFilter(filter)
                filterBitmap.value = bitmapWithFilterApplied
            }
        }
    }
}