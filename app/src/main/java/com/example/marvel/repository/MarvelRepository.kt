package com.example.marvel.repository

import com.example.marvel.model.character.CharacterModel
import com.example.marvel.model.remote.ServiceApi
import com.example.marvel.model.remote.local.MarvelDao
import javax.inject.Inject

class MarvelRepository @Inject constructor (
    private val api: ServiceApi,
    private val dao : MarvelDao

) {

    suspend fun list(nameStartsWith: String? = null) = api.getList(nameStartsWith)
    suspend fun getComics(characterId : Int) = api.getComics(characterId)

    suspend fun insert(characterModel: CharacterModel) = dao.inset(characterModel)
    fun getAll()=dao.getAll()
    suspend fun delete(characterModel: CharacterModel)=dao.delete(characterModel)

}