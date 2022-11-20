package com.rumman.securefilenotes.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.rumman.securefilenotes.R
import com.rumman.securefilenotes.data.interfaces.PermissionListener
import com.rumman.securefilenotes.databinding.FragmentAddBinding
import com.rumman.securefilenotes.ui.activities.HomeActivity
import com.rumman.securefilenotes.ui.viewmodels.HomeViewModel
import com.rumman.securefilenotes.utils.Resources
import com.rumman.securefilenotes.utils.showToast

class AddFragment : Fragment(),PermissionListener {

    private lateinit var binding: FragmentAddBinding
    private val homeViewModel : HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveFB.setOnClickListener {
            if(binding.title.text.toString().isNotEmpty() && binding.notes.text.toString().isNotEmpty()){
                (requireActivity() as HomeActivity).checkRequiredPermissions(this)
            }else{
                requireContext().showToast("Please enter all fields")
            }
        }

        addNotesObserver()

    }

    private fun addNotesObserver() {
        homeViewModel.notesObserver.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resources.Success -> {
                    (requireActivity() as HomeActivity).hideProgressBar()
                    findNavController().popBackStack()
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

    override fun permissionGranted() {
        homeViewModel.addNotes(binding.title.text.toString(),binding.notes.text.toString())
    }


}