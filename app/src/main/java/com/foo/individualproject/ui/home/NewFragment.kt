package com.foo.individualproject.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.foo.individualproject.data.model.Word
import com.foo.individualproject.databinding.FragmentNewBinding
import com.foo.individualproject.ui.adapter.WordAdapter
import kotlinx.coroutines.launch

class NewFragment : Fragment() {
    private lateinit var binding: FragmentNewBinding
    private  val viewModel: NewViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    ) {
        NewViewModel.Factory
    }
    private  lateinit var adapter: WordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNewBinding.inflate(
            layoutInflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()

        lifecycleScope.launch{
            viewModel.run {
                query.observe(viewLifecycleOwner) {
                    lifecycleScope.launch {
                        viewModel.getAll().collect {
                            adapter.setWords(it)
                            binding.tvNoCompletedWords.isInvisible = adapter.itemCount != 0
                        }
                    }
                }
                finish.collect {
                    val words = viewModel.sortWord(adapter.getWords())
                    adapter.setWords(words)
                }
            }
        }

        binding.fadAdd.setOnClickListener{
            findNavController().navigate(
                HomeFragmentDirections.homeToAddWord()
            )
        }
    }

    private fun setupAdapter() {
//        adapter = WordAdapter(listOf(Word(title = "Title", meaning = "Meaning", synonyms = "synonyms", details = "details")))
        val layoutManager = LinearLayoutManager(requireContext())
        adapter = WordAdapter(emptyList())
            adapter.listener = object : WordAdapter.Listener {
                override fun onClick(word: Word) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToSelectedWordFragment(word.id!!)
                    )
                }
            }
        binding.rvWords.adapter = adapter
        binding.rvWords.layoutManager = layoutManager
    }
}



