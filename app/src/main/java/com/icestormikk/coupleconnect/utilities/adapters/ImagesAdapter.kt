package com.icestormikk.coupleconnect.utilities.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.icestormikk.coupleconnect.database.entities.image.Image
import com.icestormikk.coupleconnect.databinding.LayoutImageCardBinding

class ImagesAdapter(
    private var images: List<Image>
): RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val imageCardBinding: LayoutImageCardBinding
    ) : RecyclerView.ViewHolder(imageCardBinding.root) {
        fun bind(source: Image) {
            with (imageCardBinding) {
                println("TEST: ${source.data}")
                image.load(source.data)
            }
        }
    }

    fun setData(images: List<Image>) {
        this.images = images
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutImageCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = this.images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = this.images[position]
        holder.bind(image)
    }
}