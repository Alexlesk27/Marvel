package com.example.marvel.ui.fragment.details

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.marvel.R
import com.example.marvel.databinding.FragmentDetailsCharacterBinding
import com.example.marvel.model.character.CharacterModel
import com.example.marvel.ui.fragment.list.adapter.ComicAdapter
import com.example.marvel.ui.stats.ResourceStates
import com.example.marvel.util.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class DetailsCharacterFragment : Fragment(),MenuProvider {
    private lateinit var binding: FragmentDetailsCharacterBinding
    private val viewModel: DetailsCharacterViewModel by viewModels()

    private val args: DetailsCharacterFragmentArgs by navArgs()
    private val comicAdapter by lazy {ComicAdapter()}
    private lateinit var characterModel: CharacterModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        characterModel = args.character
        viewModel.fetch(characterModel.id)
        setupRecyclerView()
        onLoadedCharacter(characterModel)
        collectObserver()
        descriptionCharacter()
    }

    private fun descriptionCharacter() {
        binding.tvDescriptionCharacterDetails.setOnClickListener {
            onShowDialog(characterModel)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding = FragmentDetailsCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_details, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.favorite->{
                   viewModel.insert(characterModel)
                toast(getString(R.string.saved_successfully))

            }
        }
        return true
    }

    private fun onShowDialog(characterModel: CharacterModel) {
      MaterialAlertDialogBuilder(requireContext())
          .setTitle(characterModel.name)
          .setMessage(characterModel.description)
          .setNegativeButton(getString(R.string.close_dialog)){dialog, _ ->
              dialog.dismiss()
          }
          .show()
    }

    private fun collectObserver()= lifecycleScope.launch {
       viewModel.detals.collect{  results ->
          when(results){
              is ResourceStates.Success -> {
                  binding.progressBarDetail.hide()
                results.data?.let {
                    if (it.data.result.isNotEmpty()){
                        comicAdapter.comics = it.data.result.toList()
                    }else{
                        toast(getString(R.string.empty_list_comics))
                    }
                }
              }

              is ResourceStates.Error -> {
                  binding.progressBarDetail.hide()
                  results.message?.let { message->
                      Timber.tag("DetailsCharacter").e("Error -> $message")
                      toast(getString(R.string.an_error_occurred))
                  }

              }

              is ResourceStates.Loading -> {
                binding.progressBarDetail.show()
              }
              else->{}
          }
       }
    }

    private fun onLoadedCharacter(characterModel: CharacterModel)= with(binding) {
        tvNameCharacterDetails.text = characterModel.name
        if (characterModel.description.isEmpty()){
            tvDescriptionCharacterDetails.text = requireContext().getString(R.string.text_descripition_empty)
        }else{
            tvDescriptionCharacterDetails.text = characterModel.description.limitCharacter(100)
        }

        loadImage(
            imgCharacterDetails,
            characterModel.thumbnailModel.path,
            characterModel.thumbnailModel.extension)
    }

    private fun setupRecyclerView()= with(binding) {
       rvComics.apply {
           adapter = comicAdapter
           layoutManager = LinearLayoutManager(context)
       }
    }
}