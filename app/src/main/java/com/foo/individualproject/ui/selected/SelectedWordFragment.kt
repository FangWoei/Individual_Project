package com.foo.individualproject.ui.selected

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.foo.individualproject.databinding.CompletedWordBinding
import com.foo.individualproject.databinding.DeleteWordBinding
import com.foo.individualproject.databinding.FragmentSelectedWordBinding
import kotlinx.coroutines.launch


class SelectedWordFragment : Fragment() {
    private lateinit var binding: FragmentSelectedWordBinding
    private val viewModel: SelectedViewModel by viewModels{
        SelectedViewModel.Factory
    }
    private val args: SelectedWordFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectedWordBinding.inflate(
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

        viewModel.run {
            getWordById(args.id)
            word.observe(viewLifecycleOwner){
                setWords()
                binding.btnDone.isInvisible = it.isCompleted == true
            }
        }
        lifecycleScope.launch {
            viewModel.finish.collect {
                findNavController().popBackStack(
                    findNavController().graph.startDestinationId, false
                )
            }
        }

        binding.btnDone.setOnClickListener {
            val alertView = CompletedWordBinding.inflate(layoutInflater)
            val deleteDialog = AlertDialog.Builder(requireContext())
            deleteDialog.setView(alertView.root)
            val temporaryDeleteDialog = deleteDialog.create()

            alertView.btnYes.setOnClickListener {
                viewModel.completeWords()
                findNavController().navigate(
                    SelectedWordFragmentDirections.actionSelectedWordFragmentToHomeFragment2()
                )
                Toast.makeText(
                    requireContext(),
                    "Deleted Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                temporaryDeleteDialog.dismiss()
            }

            alertView.btnNo.setOnClickListener {
                temporaryDeleteDialog.dismiss()
            }
            temporaryDeleteDialog.show()

        }

        binding.btnDelete.setOnClickListener {
            val alertView = DeleteWordBinding.inflate(layoutInflater)
            val deleteDialog = AlertDialog.Builder(requireContext())
            deleteDialog.setView(alertView.root)
            val temporaryDeleteDialog = deleteDialog.create()

            alertView.btnDelete.setOnClickListener {
                viewModel.deleteWords()
                findNavController().navigate(
                    SelectedWordFragmentDirections.actionSelectedWordFragmentToHomeFragment2()
                )
                Toast.makeText(
                    requireContext(),
                    "Deleted Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                temporaryDeleteDialog.dismiss()
            }

            alertView.btnCancel.setOnClickListener {
                temporaryDeleteDialog.dismiss()
            }
            temporaryDeleteDialog.show()

        }

        binding.btnUpdate.setOnClickListener {
            findNavController().navigate(
                SelectedWordFragmentDirections.actionSelectedWordFragmentToUpdateWordFragment(args.id)
            )
        }
    }

}