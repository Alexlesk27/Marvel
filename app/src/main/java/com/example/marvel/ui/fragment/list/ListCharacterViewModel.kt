package com.example.marvel.ui.fragment.list

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
class ListCharacterViewModel @Inject constructor(
    private val repository: MarvelRepository
) : ViewModel() {

    private val _list =
        MutableStateFlow<ResourceStates<CharacterModelResponse>>(ResourceStates.Loading())
    val list: StateFlow<ResourceStates<CharacterModelResponse>> = _list

    init {
        fetch()
    }

    private fun fetch() = viewModelScope.launch {
        safeFetch()
    }

    private suspend fun safeFetch() {
        try {
            val response = repository.list()
            _list.value = handleResponse(response)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _list.value =
                    ResourceStates.Error("Erro de conexão com a internet")
                else -> _list.value = ResourceStates.Error("Falha na converão de dados")
            }
        }
    }

    private fun handleResponse(response: Response<CharacterModelResponse>): ResourceStates<CharacterModelResponse> {
        if (response.isSuccessful) {
            response.body()?.let { values ->
                return ResourceStates.Success(values)
            }
        }

        return ResourceStates.Error(response.message())
    }
}