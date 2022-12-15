package com.imagefiltter.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imagefiltter.databinding.ItemContainerSavedImageLayoutBinding
import com.imagefiltter.listeners.SavedImageListener
import java.io.File

class SavedImagesAdapter(private val savedImages: List<Pair<File, Bitmap>>,
private val savedImageListener: SavedImageListener):
    RecyclerView.Adapter<SavedImagesAdapter.SavedImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImageViewHolder {
        val binding = ItemContainerSavedImageLayoutBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return SavedImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedImageViewHolder, position: Int) {
        with(holder){
            with(savedImages[position]){
                binding.savedImage.setImageBitmap(second)
                binding.savedImage.setOnClickListener{
                    savedImageListener.onImageClickListener(first)
                }
            }
        }
    }

    override fun getItemCount() = savedImages.size

    inner class SavedImageViewHolder(val binding: ItemContainerSavedImageLayoutBinding): RecyclerView.ViewHolder(binding.root)
}