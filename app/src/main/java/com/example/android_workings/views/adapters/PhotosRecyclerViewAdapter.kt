package com.example.android_workings.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android_workings.data.models.PhotoModel
import com.example.android_workings.databinding.ItemRecyclerviewBinding

class PhotosRecyclerViewAdapter(
    private val photoList: ArrayList<PhotoModel>,
    private val listener: RecyclerItemClickListener
) : RecyclerView.Adapter<PhotosRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photoModel: PhotoModel, listener: RecyclerItemClickListener) {
            binding.photo = photoModel
            binding.listener = listener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerviewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(photoList[position], listener)


    override fun getItemCount(): Int = photoList.size

    fun updateList(list: ArrayList<PhotoModel>) {
        photoList.addAll(list)
        notifyDataSetChanged()
    }

}

interface RecyclerItemClickListener {
    fun onClick(photoModel: PhotoModel)
}