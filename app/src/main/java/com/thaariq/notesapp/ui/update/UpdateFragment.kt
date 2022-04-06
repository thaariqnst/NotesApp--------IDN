package com.thaariq.notesapp.ui.update

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.thaariq.notesapp.R
import com.thaariq.notesapp.data.entity.Notes
import com.thaariq.notesapp.databinding.FragmentUpdateBinding
import com.thaariq.notesapp.ui.NotesViewModel
import com.thaariq.notesapp.utils.ExtensionFunctions.setActionBar
import com.thaariq.notesapp.utils.HelperFunctions.spinnerListener
import com.thaariq.notesapp.utils.HelperFunctions.parseToPriority
import java.text.SimpleDateFormat
import java.util.*

class UpdateFragment : Fragment() {

    private var _binding : FragmentUpdateBinding? = null
    private val binding get() = _binding as FragmentUpdateBinding

    private val navArgs by navArgs<UpdateFragmentArgs>()

    private val updateViewModel by viewModels<NotesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // inisialisasi variabel databinding yang ada di XML
        binding.safeArgs = navArgs

        setHasOptionsMenu(true)

        binding.apply {
            toolbarUpdate.setActionBar(requireActivity())
            spinnerPrioritiesUpdate.onItemSelectedListener = spinnerListener(context, binding.priorityIndicator)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save,menu)

        val action = menu.findItem(R.id.menu_save)
        action.actionView.findViewById<AppCompatImageButton>(R.id.btn_save).setOnClickListener{
            updateNote()
        }
    }

    private fun updateNote() {
        binding.apply {
            val title = edtTitleUpdate.text.toString()
            val priority = spinnerPrioritiesUpdate.selectedItem.toString()
            val descriptionCompat = edtDescriptionUpdate.text.toString()

            val calendar = Calendar.getInstance().time
            val date = SimpleDateFormat("dd MMM yyy", Locale.getDefault()).format(calendar)


            val notes = Notes(
                navArgs.currentItem.id,
                title,
                parseToPriority(priority, context),
                descriptionCompat,
                date
            )

            if (edtTitleUpdate.text.isEmpty() || edtDescriptionUpdate.text.isEmpty()){
                edtTitleUpdate.error = "Please fill in the fields"
                edtDescriptionUpdate.error = "Please fill in the fields"
            }else{
                updateViewModel.updateNote(notes)
                val action = UpdateFragmentDirections.actionUpdateFragmentToDetailFragment(notes)
                findNavController().navigate(action)
                Toast.makeText(context,"Note successfully updated", Toast.LENGTH_SHORT).show()
                Log.i("updateNote", "updateNote: $notes")
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
