package com.example.marvel.ui.fragment.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel.model.character.CharacterModelResponse
import com.example.marvel.repository.MarvelRepository
import com.example.marvel.ui.stats.ResourceStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchCharacterViewModel @Inject constructor(
    private val repository: MarvelRepository
) : ViewModel() {

    private var _search =
        MutableStateFlow<ResourceStates<CharacterModelResponse>>(ResourceStates.Empty())
     val search: StateFlow<ResourceStates<CharacterModelResponse>> = _search



    fun fetch(nameStartsWith: String) = viewModelScope.launch {
        safeFetch(nameStartsWith)
    }

    private suspend fun safeFetch(nameStartsWith: String) {
        _search.value = ResourceStates.Loading()
        try {
            val response = repository.list(nameStartsWith)
            _search.value = handleResponse(response)
        }catch (t : Throwable){
            when(t){
                is IOException -> _search.value = ResourceStates.Error("Erro na rede")
                else -> _search.value = ResourceStates.Error("Erro na conversão")

            }
        }
    }

    private fun handleResponse(response: Response<CharacterModelResponse>): ResourceStates<CharacterModelResponse> {
        if (response.isSuccessful) {
            response.body()?.let { value ->
                return ResourceStates.Success(value)
            }
        }
        return ResourceStates.Error(response.message())

    }

}