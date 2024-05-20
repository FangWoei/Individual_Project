package com.foo.individualproject.ui.update

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.foo.individualproject.databinding.FragmentUpdateWordBinding
import kotlinx.coroutines.launch

class UpdateWordFragment : Fragment() {
    private lateinit var binding: FragmentUpdateWordBinding
    private val viewModel: UpdateWordViewModel by viewModels {
        UpdateWordViewModel.Factory
    }
    private val args: UpdateWordFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateWordBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        lifecycleScope.launch {
            viewModel.run {
                getWordById(args.id)
                word.observe(viewLifecycleOwner) { setWord() }
                edit.collect{ findNavController().navigate(
                    UpdateWordFragmentDirections.UpdateToHome()
                )}
            }
        }
        viewModel.snackbar.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}