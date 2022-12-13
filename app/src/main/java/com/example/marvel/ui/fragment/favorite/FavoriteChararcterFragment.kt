package com.example.marvel.ui.fragment.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.R
import com.example.marvel.databinding.FragmentFavoriteChararcterBinding
import com.example.marvel.ui.fragment.list.adapter.CharacterAdapter
import com.example.marvel.ui.stats.ResourceStates
import com.example.marvel.util.hide
import com.example.marvel.util.show
import com.example.marvel.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteChararcterFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteChararcterBinding
    private val viewModel: FavoriteCharacterViewModel by viewModels()
    private val characterAdapter by lazy { CharacterAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteChararcterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerview()
        clickAdapter()
        observer()
    }

    private fun observer() = lifecycleScope.launch {
        viewModel.favorites.collect { resource ->
            when (resource) {
                is ResourceStates.Success -> {
                    resource.data?.let {
                        binding.tvEmptyList.hide()
                        characterAdapter.character = it.toList()
                    }
                }

                is ResourceStates.Empty -> {
                    binding.tvEmptyList.show()
                }

                else -> Unit
            }
        }
    }

    private fun clickAdapter() {
        characterAdapter.setOnclickListener {
            val action =
                FavoriteChararcterFragmentDirections.actionFavoriteChararcterFragmentToDetailsCharacterFragment(
                    it
                )
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerview() = with(binding) {
        rvFavoriteCharacter.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(context)
        }
        ItemTouchHelper(itemTouchHelperCallback()).attachToRecyclerView(rvFavoriteCharacter)
    }

    private fun itemTouchHelperCallback(): ItemTouchHelper.SimpleCallback{
        return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
               return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val character = characterAdapter.getCharacterPosition(viewHolder.adapterPosition)
                viewModel.delete(character).also {
                    toast(getString(R.string.message_delete_character))
                }
            }

        }
    }


}