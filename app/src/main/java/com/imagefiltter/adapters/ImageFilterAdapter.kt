package com.imagefiltter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.imagefiltter.R
import com.imagefiltter.data.ImageFilter
import com.imagefiltter.databinding.ItemContainerFilterBinding
import com.imagefiltter.listeners.ImageFilterListener

class ImageFilterAdapter(private val imageFilters: List<ImageFilter>,
private val imageFilterListener: ImageFilterListener):
    RecyclerView.Adapter<ImageFilterAdapter.ImageFilterViewHolder>() {

    private var selectedFilterPosition = 0
    private var previouslySelectedPosition = 0

    inner class ImageFilterViewHolder(val binding: ItemContainerFilterBinding):
        ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFilterViewHolder {
        val binding = ItemContainerFilterBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return ImageFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageFilterViewHolder, position: Int) {
        with(holder){
            with(imageFilters[position]){
                binding.imageFilterPreview.setImageBitmap(filterPreview)
                binding.textFilterName.text = name
                binding.root.setOnClickListener {
                    if (position != selectedFilterPosition){
                        imageFilterListener.onFilterSelectedListener(this)
                        previouslySelectedPosition = selectedFilterPosition
                        selectedFilterPosition = position
                        with(this@ImageFilterAdapter){
                            notifyItemChanged(previouslySelectedPosition, Unit)
                            notifyItemChanged(selectedFilterPosition, Unit)
                        }
                    }
                }
            }
            binding.textFilterName.setTextColor(
                ContextCompat.getColor(
                    binding.textFilterName.context,
                    if (selectedFilterPosition == position){
                        R.color.primaryDark
                    }else{
                        R.color.primaryText
                    }
                )
            )
        }
    }

    override fun getItemCount() = imageFilters.size
}