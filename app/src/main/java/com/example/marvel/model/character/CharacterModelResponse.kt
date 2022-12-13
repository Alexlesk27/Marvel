package com.example.marvel.model.character

import com.example.marvel.model.character.CharacterModelData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CharacterModelResponse(
    @SerializedName("data") val data: CharacterModelData
):Serializable