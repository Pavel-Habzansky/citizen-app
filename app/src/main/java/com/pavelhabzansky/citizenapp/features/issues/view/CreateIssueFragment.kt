package com.pavelhabzansky.citizenapp.features.issues.view

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.*
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.fragment.findParentNavController
import com.pavelhabzansky.citizenapp.databinding.FragmentCreateIssueBinding
import com.pavelhabzansky.citizenapp.features.issues.states.CreateIssueViewStates
import com.pavelhabzansky.citizenapp.features.issues.view.vm.CreateIssueViewModel
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueTypeVO
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO
import com.pavelhabzansky.data.features.issues.model.Gps
import kotlinx.android.synthetic.main.fragment_create_issue.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class CreateIssueFragment : BaseFragment() {

    private lateinit var binding: FragmentCreateIssueBinding

    private val viewModel by sharedViewModel<CreateIssueViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_issue,
            container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lat = arguments?.getDouble(ARG_KEY_LAT)
        val lng = arguments?.getDouble(ARG_KEY_LNG)

        if (lat == null || lng == null) {
            Toast.makeText(context, "Poloha není dostupná", Toast.LENGTH_LONG).show()
            findParentNavController().navigateUp()
            return
        }

        registerEvents()

        viewModel.position = Gps(lat = lat, lng = lng)

        binding.createButton.setOnClickListener { createIssue() }
        binding.addPhotoImg.isEnabled = false
        binding.addPhotoImg.setOnClickListener { openCamera() }

        val spinnerAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            IssueTypeVO.values().map { getString(it.text) }
        )
        binding.typeSpinner.adapter = spinnerAdapter

        viewModel.requestCameraPermission()
    }

    private fun registerEvents() {
        viewModel.createIssueViewState.observe(this, Observer {
            updateViewState(it)
        })

        viewModel.createIssueErrorState.observe(this, Observer {
            Timber.e(it.t, "Error occured")
        })
    }

    private fun updateViewState(event: CreateIssueViewStates) {
        when (event) {
            is CreateIssueViewStates.AttachmentSetEvent -> {
                event.attachment?.let { binding.addPhotoImg.setImageBitmap(it) }
            }
            is CreateIssueViewStates.CameraPermissionNotGranted -> {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQ
                )
            }
            is CreateIssueViewStates.CameraPermissionGranted -> {
                binding.addPhotoImg.isEnabled = true
            }
        }
    }

    private fun createIssue() {
        if (binding.titleInput.text?.isBlank() == true) {
            Toast.makeText(context, R.string.issue_create_invalid_title, Toast.LENGTH_LONG).show()
            return
        }

        if (binding.descriptionInput.text?.isBlank() == true) {
            Toast.makeText(context, R.string.issue_create_invalid_description, Toast.LENGTH_LONG)
                .show()
            return
        }

        if (binding.typeSpinner.selectedItem == null) {
            Toast.makeText(context, R.string.issue_create_invalid_type, Toast.LENGTH_LONG).show()
            return
        }

        if (viewModel.attachment == null) {
            Toast.makeText(context, R.string.issue_create_invalid_attachment, Toast.LENGTH_LONG)
                .show()
            return
        }

        val type = IssueTypeVO.values()[binding.typeSpinner.selectedItemPosition]
        val issue = IssueVO(
            title = binding.titleInput.text.toString(),
            createTime = System.currentTimeMillis(),
            type = type,
            description = binding.descriptionInput.text.toString(),
            lat = viewModel.position.lat,
            lng = viewModel.position.lng,
            img = ""
        )

        viewModel.createIssue(issue)
        findParentNavController().navigateUp()
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(requireActivity().packageManager)?.also {
                activity?.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
}