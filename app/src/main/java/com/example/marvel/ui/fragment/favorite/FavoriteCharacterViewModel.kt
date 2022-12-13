package com.example.marvel.ui.fragment.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvel.model.character.CharacterModel
import com.example.marvel.repository.MarvelRepository
import com.example.marvel.ui.stats.ResourceStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class FavoriteCharacterViewModel @Inject constructor(
   private val repository: MarvelRepository
): ViewModel() {

    private val _favorites = MutableStateFlow<ResourceStates<List<CharacterModel>>>(ResourceStates.Empty())
    val favorites: StateFlow<ResourceStates<List<CharacterModel>>> = _favorites

    init {
       fetch()
    }

  private fun fetch()= viewModelScope.launch {
        repository.getAll().collectLatest{
            if (it.isNullOrEmpty()){
                _favorites.value = ResourceStates.Empty()
            }else{
                _favorites.value = ResourceStates.Success(it)
            }
        }
    }

    fun delete(characterModel: CharacterModel)= viewModelScope.launch{
        repository.delete(characterModel)
    }
}