package com.example.marvel.ui.fragment.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.R
import com.example.marvel.databinding.ItemCharacterBinding
import com.example.marvel.model.character.CharacterModel
import com.example.marvel.util.limitCharacter
import com.example.marvel.util.loadImage

class CharacterAdapter() : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    inner class CharacterViewHolder(val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffUtilCallbacks = object : DiffUtil.ItemCallback<CharacterModel>() {
        override fun areItemsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.description == newItem.description

        }
    }

    private val differ = AsyncListDiffer(this, diffUtilCallbacks)

    var character: List<CharacterModel>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = character.size


    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = character[position]
        holder.binding.apply {
            tvNameCharacter.text = character.name

            if (character.description == "") {
                tvDescriptionCharacter.text =
                    holder.itemView.context.getString(R.string.text_descripition_empty)
            } else {
                tvDescriptionCharacter.text = character.description.limitCharacter(100)
            }

            loadImage(
                imgCharacter,
                character.thumbnailModel.path,
                character.thumbnailModel.extension
            )
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(character)
            }
        }

    }

    private var onItemClickListener: ((CharacterModel) -> Unit)? = null

    fun setOnclickListener(listener: (CharacterModel) -> Unit) {
        onItemClickListener = listener
    }

    fun getCharacterPosition(position: Int): CharacterModel {
        return character[position]
    }

}