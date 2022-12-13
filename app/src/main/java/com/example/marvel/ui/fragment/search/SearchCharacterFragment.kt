package com.example.marvel.ui.fragment.search

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marvel.R
import com.example.marvel.databinding.FragmentSearchCharacterBinding
import com.example.marvel.ui.fragment.list.adapter.CharacterAdapter
import com.example.marvel.ui.stats.ResourceStates
import com.example.marvel.util.Constant.DEFAULT_QUERY
import com.example.marvel.util.Constant.LAST_SEARCH_QUERY
import com.example.marvel.util.hide
import com.example.marvel.util.show
import com.example.marvel.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SearchCharacterFragment : Fragment() {
    private lateinit var binding: FragmentSearchCharacterBinding

   private val viewModel: SearchCharacterViewModel by viewModels()
    private val characterAdapter by lazy {CharacterAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerview()
        clickAdapter()


        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY)?: DEFAULT_QUERY
        searchInit(query)
        collectObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }



    private fun collectObserver() = lifecycleScope.launch{
        viewModel.search.collect{result ->
           when(result){
               is ResourceStates.Success->{
                    binding.progressbarSearch.hide()
                   result.data?.let {
                       characterAdapter.character = it.data.results.toList()
                   }
               }

               is ResourceStates.Error->{
                    binding.progressbarSearch.hide()
                   result.message?.let { message ->
                       toast(getString(R.string.text_descripition_empty))
                       Timber.tag("SearchFragment").e("Error -> $message")
                   }
               }

               is ResourceStates.Loading->{
                     binding.progressbarSearch.show()
               }
               else -> {

               }
           }
        }
    }

    private fun searchInit(query: String)= with(binding) {
        edSearchCharacter.setText(query)
        edSearchCharacter.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO){
                updateCharacterList()
               true
            }else{
                false
            }
        }
        edSearchCharacter.setOnKeyListener { _, keyCode, event ->
           if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
               updateCharacterList()
               true
           }else{
               false
           }
        }
    }

    private fun updateCharacterList()= with(binding) {
       edSearchCharacter.editableText.trim().let {
           if (it.isNotEmpty()){
               searchQuery(it.toString())
           }
       }
    }

    private fun searchQuery(query: String) {
        viewModel.fetch(query)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, binding.edSearchCharacter.editableText.trim().toString())
    }

    private fun clickAdapter() {
       characterAdapter.setOnclickListener {
           val action = SearchCharacterFragmentDirections.actionSearchCharacterFragmentToDetailsCharacterFragment(it)
           findNavController().navigate(action)
       }
    }

    private fun setRecyclerview() = with(binding) {
         rvSearchCharacter.apply {
             adapter = characterAdapter
             layoutManager = LinearLayoutManager(context)
         }
    }

}