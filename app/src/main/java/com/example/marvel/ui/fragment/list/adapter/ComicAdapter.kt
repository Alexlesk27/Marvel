package com.example.marvel.ui.fragment.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marvel.databinding.ItemComicBinding
import com.example.marvel.model.comic.ComicModel
import com.example.marvel.util.limitCharacter
import com.example.marvel.util.loadImage

class ComicAdapter() : RecyclerView.Adapter<ComicAdapter.ComicViewHolder>() {

    inner class ComicViewHolder(val binding: ItemComicBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffUtilCallbacks = object : DiffUtil.ItemCallback<ComicModel>() {
        override fun areItemsTheSame(oldItem: ComicModel, newItem: ComicModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: ComicModel, newItem: ComicModel): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.description == newItem.description
                    && oldItem.title == newItem.title
                    && oldItem.thumbnailModel.path == newItem.thumbnailModel.path
                    && oldItem.thumbnailModel.extension == newItem.thumbnailModel.extension
        }
    }

    private val differ = AsyncListDiffer(this, diffUtilCallbacks)

    var comics: List<ComicModel>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        return ComicViewHolder(
            ItemComicBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = comics.size

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        val comic = comics[position]
        holder.binding.apply {
            tvNameComic.text = comic.title
            tvDescriptionComic.text = comic.description.limitCharacter(100)
            loadImage(
                imgComic,
                comic.thumbnailModel.path,
                comic.thumbnailModel.extension)
        }
    }
}