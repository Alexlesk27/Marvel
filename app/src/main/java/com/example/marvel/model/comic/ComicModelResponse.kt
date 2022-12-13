package com.example.marvel.model.comic

import com.example.marvel.model.ThumbnailModel
import com.example.marvel.model.character.CharacterModelData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ComicModelResponse(
    @SerializedName("data") val data: ComicModelData
) : Serializable
