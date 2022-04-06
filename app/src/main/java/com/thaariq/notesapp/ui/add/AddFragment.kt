package com.thaariq.notesapp.ui.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.thaariq.notesapp.R
import com.thaariq.notesapp.data.entity.Notes
import com.thaariq.notesapp.databinding.FragmentAddBinding
import com.thaariq.notesapp.ui.NotesViewModel
import com.thaariq.notesapp.ui.ViewModelFactory
import com.thaariq.notesapp.utils.ExtensionFunctions.setActionBar
import com.thaariq.notesapp.utils.HelperFunctions
import com.thaariq.notesapp.utils.HelperFunctions.parseToPriority
import java.text.SimpleDateFormat
import java.util.*


class AddFragment : Fragment() {

    private var _binding : FragmentAddBinding? = null
    private val binding get() = _binding as FragmentAddBinding

    private val addViewModel by viewModels<NotesViewModel>()

//    private var _addViewModel : NotesViewModel? =null
//    private val addViewModel get() = _addViewModel as NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)


//        _addViewModel = activity?.let { obtainViewModel(it) }



        binding.apply {
            toolbarAdd.setActionBar(requireActivity())
            spinnerPriorities.onItemSelectedListener =
                HelperFunctions.spinnerListener(context,binding.priorityIndicator)

        }
    }
    private fun obtainViewModel(activity: FragmentActivity) : NotesViewModel{
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[NotesViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save, menu)

        val action = menu.findItem(R.id.menu_save)
        action.actionView.findViewById<AppCompatImageButton>(R.id.btn_save).setOnClickListener {
            insertNotes()

        }
    }

    private fun insertNotes(){
        binding.apply {
            val title = edtTitle.text.toString()
            val priority = spinnerPriorities.selectedItem.toString()
            val descriptionCompat = edtDescription.text.toString()

            val calendar = Calendar.getInstance().time
            val date = SimpleDateFormat("dd MMM yyy", Locale.getDefault()).format(calendar)


            val notes = Notes(
                0,
                title,
                parseToPriority(priority,context),
                descriptionCompat,
                date
            )

            if (edtTitle.text.isEmpty() || edtDescription.text.isEmpty()){
                edtTitle.error = "Please fill in the fields"
                edtDescription.error = "Please fill in the fields"
            }else{
                addViewModel.insertData(notes)
                findNavController().navigate(R.id.action_addFragment_to_homeFragment)
                Toast.makeText(context,"Note successfully created", Toast.LENGTH_SHORT).show()
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}