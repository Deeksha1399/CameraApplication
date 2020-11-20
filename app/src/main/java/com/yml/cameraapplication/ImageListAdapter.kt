package com.yml.cameraapplication

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView


class ImageAdapter(private val imagesList: List<String>) :
    RecyclerView.Adapter<ImageAdapter.ResponseViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResponseViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.image_item_list, parent, false)
        return ResponseViewHolder(itemView)
    }

    class ResponseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val responseItemView: ImageView = itemView.findViewById(R.id.images)
    }

    override fun onBindViewHolder(holder: ResponseViewHolder, position: Int) {
        val bitmap = BitmapFactory.decodeFile(imagesList[position])
        holder.responseItemView.setImageBitmap(bitmap)
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }
}
