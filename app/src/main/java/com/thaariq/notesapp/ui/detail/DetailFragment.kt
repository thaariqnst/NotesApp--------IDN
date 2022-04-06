package com.thaariq.notesapp.ui.detail

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.thaariq.notesapp.R
import com.thaariq.notesapp.data.entity.Notes
import com.thaariq.notesapp.databinding.FragmentDetailBinding
import com.thaariq.notesapp.ui.NotesViewModel
import com.thaariq.notesapp.utils.ExtensionFunctions.setActionBar

class DetailFragment : Fragment() {

    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding as FragmentDetailBinding

    private val navArgs by navArgs <DetailFragmentArgs>()

    private val detailViewModel by viewModels<NotesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)


        binding.toolbarDetail.setActionBar(requireActivity())
        binding.safeArgs = navArgs
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_edit -> {
                val action = DetailFragmentDirections.actionDetailFragmentToUpdateFragment(
                    Notes(
                        navArgs.currentItem.id,
                        navArgs.currentItem.title,
                        navArgs.currentItem.priority,
                        navArgs.currentItem.description,
                        navArgs.currentItem.date,
                    )
                )
                findNavController().navigate(action)
            }
            R.id.action_delete -> confirmDeleteNote()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDeleteNote() {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle("Delete '${navArgs.currentItem.title}'?")
                .setMessage("Are you sure want to remove '${navArgs.currentItem.title}'")
                .setPositiveButton("YES"){ _, _ ->
                    findNavController().navigate(R.id.action_detailFragment_to_homeFragment)
                    detailViewModel.deleteNote(navArgs.currentItem)
                    Toast.makeText(it,"Successfully delete '${navArgs.currentItem.title}'", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No"){_,_ ->}
                .setNeutralButton("Cancel"){_,_ ->}
                .create()
                .show()
        }
    }



}