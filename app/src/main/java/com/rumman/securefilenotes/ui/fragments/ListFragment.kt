package com.rumman.securefilenotes.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.rumman.securefilenotes.R
import com.rumman.securefilenotes.data.interfaces.PermissionListener
import com.rumman.securefilenotes.databinding.FragmentListBinding
import com.rumman.securefilenotes.ui.activities.HomeActivity
import com.rumman.securefilenotes.ui.adapter.NotesAdapter
import com.rumman.securefilenotes.ui.viewmodels.HomeViewModel
import com.rumman.securefilenotes.utils.Resources
import com.rumman.securefilenotes.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Collections

@AndroidEntryPoint
class ListFragment : Fragment(),PermissionListener,SearchView.OnQueryTextListener {

    private lateinit var binding : FragmentListBinding
    private lateinit var notesAdapter: NotesAdapter
    private val homeViewModel : HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_list,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchNotes.setOnQueryTextListener(this)
        binding.addFB.setOnClickListener {
            binding.searchNotes.setQuery("",true)
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        setupUI()
        filesObserver()
    }


    private fun filesObserver() {
        homeViewModel.fileObserver.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resources.Success -> {
                    (requireActivity() as HomeActivity).hideProgressBar()
                    notesAdapter.setData(ArrayList(it.data))
                }
                is Resources.Error -> {
                    (requireActivity() as HomeActivity).hideProgressBar()
                    requireContext().showToast(it.error)
                }
                is Resources.Loading -> {
                    (requireActivity() as HomeActivity).showProgressBar()
                }
                else -> {
                    (requireActivity() as HomeActivity).hideProgressBar()
                }
            }
        })
    }

    private fun setupUI() {
        notesAdapter = NotesAdapter()
        binding.notesList.apply {
            adapter = notesAdapter
        }
    }

    override fun permissionGranted() {
        homeViewModel.getFiles()
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as HomeActivity).checkRequiredPermissions(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        notesAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        notesAdapter.filter.filter(newText)
        return false
    }
}