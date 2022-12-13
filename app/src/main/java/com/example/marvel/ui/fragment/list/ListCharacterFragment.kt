package com.example.marvel.ui.fragment.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.example.marvel.databinding.FragmentListCharacterBinding
import com.example.marvel.ui.fragment.list.adapter.CharacterAdapter
import com.example.marvel.ui.stats.ResourceStates
import com.example.marvel.util.hide
import com.example.marvel.util.show
import com.example.marvel.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListCharacterFragment : Fragment() {
  private lateinit var binding: FragmentListCharacterBinding
  private lateinit var  characterAdapter: CharacterAdapter
   val listCharacterViewModel: ListCharacterViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Recyclerview()
        clickAdapter()
        obseverList()
    }

    private fun obseverList()= lifecycleScope.launch {
       listCharacterViewModel.list.collect{ resources ->
           when(resources){
               is ResourceStates.Success->{
                   resources.data?.let { values ->
                       binding.progressCircular.hide()
                       characterAdapter.character = values.data.results.toList()
                   }
               }
               is ResourceStates.Error->{
                   binding.progressCircular.hide()
                   toast(resources.message.toString())

               }
               is ResourceStates.Loading->{
                   binding.progressCircular.show()
               }
               else -> Unit
           }
       }
    }

    private fun clickAdapter() {
        characterAdapter.setOnclickListener {
           val action = ListCharacterFragmentDirections.actionListCharacterFragmentToDetailsCharacterFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun Recyclerview() {
        binding.rvCharacters.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL,false
        )
        characterAdapter = CharacterAdapter()
        binding.rvCharacters.adapter = characterAdapter

    }


}