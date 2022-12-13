package com.example.marvel.ui.fragment.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel.model.character.CharacterModel
import com.example.marvel.model.comic.ComicModelResponse
import com.example.marvel.repository.MarvelRepository
import com.example.marvel.ui.stats.ResourceStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.handleCoroutineException
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailsCharacterViewModel @Inject constructor(
    private val repository: MarvelRepository
): ViewModel() {

    private val _details = MutableStateFlow<ResourceStates<ComicModelResponse>>(ResourceStates.Loading())
    val detals: StateFlow<ResourceStates<ComicModelResponse>> = _details


    fun fetch(characterId: Int)= viewModelScope.launch{
        safeFetch(characterId)
    }

    private suspend fun safeFetch(characterId: Int) {
        _details.value = ResourceStates.Loading()

        try {
            val response = repository.getComics(characterId)
            _details.value = handleResponse(response)
        }catch (t : Throwable){
            when(t){
                is IOException -> _details.value =ResourceStates.Error("Erro de rede ou de conexão com a internet")
                else -> _details.value = ResourceStates.Error("Erro na conversão")
            }
        }
    }

    private fun handleResponse(response: Response<ComicModelResponse>): ResourceStates<ComicModelResponse> {
        if (response.isSuccessful){
            response.body()?.let {
                return ResourceStates.Success(it)
            }
        }
        return ResourceStates.Error(response.message())

    }

    fun insert(characterModel: CharacterModel)= viewModelScope.launch {
        repository.insert(characterModel)
    }
}