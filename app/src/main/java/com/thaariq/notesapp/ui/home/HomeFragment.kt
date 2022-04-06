package com.thaariq.notesapp.ui.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.thaariq.notesapp.ui.MainActivity
import com.thaariq.notesapp.R
import com.thaariq.notesapp.data.entity.Notes
import com.thaariq.notesapp.databinding.FragmentHomeBinding
import com.thaariq.notesapp.ui.NotesViewModel
import com.thaariq.notesapp.utils.HelperFunctions
import com.thaariq.notesapp.utils.HelperFunctions.checkIsDataEmpty

class HomeFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding

    private val homeViewModel by viewModels<NotesViewModel>()

    private var _currentData : List<Notes>? = null
    private val currentData get() = _currentData as List<Notes>

    private val homeAdapter by lazy { HomeAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initialisation for the variable inside fragment_home
        binding.mHelperFunctions = HelperFunctions

        setHasOptionsMenu(true)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.apply {

            toolbarHome.apply {
                (requireActivity() as MainActivity).setSupportActionBar(this)
                setupWithNavController(navController, appBarConfiguration)
            }

            fabAdd.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_addFragment)
            }


        }

        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        binding.rvHome.apply {
            homeViewModel.getAllData().observe(viewLifecycleOwner){
                checkIsDataEmpty(it)
                homeAdapter.setData(it)
                _currentData = it
            }
            adapter = homeAdapter
            layoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
            swipeToDelete(this)
        }
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val search = menu.findItem(R.id.menu_search)
        val searchAction = search.actionView as SearchView
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_priority_high -> homeViewModel.sortByHighPriority().observe(this){dataHigh ->
                homeAdapter.setData(dataHigh)
            }

            R.id.menu_priority_low -> homeViewModel.sortByLowPriority().observe(this){dataLow ->
                homeAdapter.setData(dataLow)
            }
            R.id.menu_delete_all -> confirmDelete()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDelete(){

        if (currentData.isEmpty()){
            AlertDialog.Builder(requireContext())
                .setTitle("No Notes")
                .setMessage("There is no Note")
                .setPositiveButton("Close"){dialog,_ ->
                    dialog.dismiss()
                }
                .show()
        }else{
        AlertDialog.Builder(requireContext())
            .setTitle("Delete All Notes")
            .setMessage("Are you sure you want to delete?")
            .setPositiveButton("Yes"){_, _ ->
                homeViewModel.deleteAllData()
                Toast.makeText(requireContext(),"Successfully deleted all data", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No"){dialog,_ ->
                dialog.dismiss()
            }
            .show()
        }
    }



    override fun onQueryTextSubmit(query: String?): Boolean {
        val querySearch = "%$query%"
            query?.let {
                homeViewModel.searchByQuery(querySearch).observe(this){
                    homeAdapter.setData(it)
                }
            }

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val querySearch = "%$newText%"
        newText?.let {
            homeViewModel.searchByQuery(querySearch).observe(this){
                homeAdapter.setData(it)
            }
        }

        return true
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDelete = object  : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = homeAdapter.listNotes[viewHolder.adapterPosition]
                homeViewModel.deleteNote(deletedItem)
                restoredData(viewHolder.itemView,deletedItem)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoredData(view: View, deletedItem: Notes) {
        val snackBar = Snackbar.make(
            view, "Deleted : '${deletedItem.title}'", Snackbar.LENGTH_LONG
        )
        snackBar.setTextColor(ContextCompat.getColor(view.context, R.color.black))
        snackBar.setAction("Undo"){
            homeViewModel.insertData(deletedItem)
        }
        snackBar.setTextColor(ContextCompat.getColor(view.context, R.color.black))
        snackBar.show()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}