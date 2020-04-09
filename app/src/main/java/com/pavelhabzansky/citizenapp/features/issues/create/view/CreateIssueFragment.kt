package com.pavelhabzansky.citizenapp.features.issues.create.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.*
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.fragment.findParentNavController
import com.pavelhabzansky.citizenapp.core.fragment.toast
import com.pavelhabzansky.citizenapp.databinding.FragmentCreateIssueBinding
import com.pavelhabzansky.citizenapp.features.issues.create.states.CreateIssueViewStates
import com.pavelhabzansky.citizenapp.features.issues.create.view.vm.CreateIssueViewModel
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueTypeVO
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO
import com.pavelhabzansky.data.features.issues.model.Gps
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
            toast("Poloha není dostupná")
            findParentNavController().navigateUp()
            return
        }

        registerEvents()

        viewModel.position = Gps(lat = lat, lng = lng)

        binding.createButton.setOnClickListener { createIssue() }
        binding.addGalleryImg.setOnClickListener { openImageChooser() }
        binding.addPhotoImg.setOnClickListener { openCamera() }
        binding.addPhotoImg.isEnabled = false

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
            is CreateIssueViewStates.AttachmentSetEvent -> event.attachment?.let {
                binding.addGalleryImg.hide()
                binding.addPhotoImg.setPadding(0)
                binding.addPhotoImg.setImageBitmap(it)
            }
            is CreateIssueViewStates.CameraPermissionNotGranted -> {
                ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.CAMERA),
                        CAMERA_PERMISSION_REQ
                )
            }
            is CreateIssueViewStates.CameraPermissionGranted -> binding.addPhotoImg.isEnabled = true
        }
    }

    private fun createIssue() {
        if (binding.titleInput.text?.isBlank() == true) {
            toast(R.string.issue_create_invalid_title)
            return
        }

        if (binding.descriptionInput.text?.isBlank() == true) {
            toast(R.string.issue_create_invalid_description)
            return
        }

        if (binding.typeSpinner.selectedItem == null) {
            toast(R.string.issue_create_invalid_type)
            return
        }

        if (viewModel.attachment == null) {
            toast(R.string.issue_create_invalid_attachment)
            return
        }

        val type = IssueTypeVO.values()[binding.typeSpinner.selectedItemPosition]
        val issue = IssueVO(
                issueTitle = binding.titleInput.text.toString(),
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

    private fun openImageChooser() {
        val intent = Intent().also {
            it.type = CHOOSER_TYPE_IMAGES
            it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            it.action = Intent.ACTION_GET_CONTENT
        }

        startActivityForResult(
                Intent.createChooser(intent, "CHOOSER"),
                IMAGE_CHOOSER_REQ
        )
    }

    private fun readBytes(context: Context, uri: Uri) =
            context.contentResolver.openInputStream(uri)?.buffered()?.use { it.readBytes() }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.i("REQUEST_CODE [$requestCode], RESULT CODE [$resultCode]")
        when (requestCode) {
            IMAGE_CHOOSER_REQ -> {
                Timber.i("Load image from gallery")
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        data?.data?.let {
                            val imgData = readBytes(requireContext(), it)
                            val bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData?.size
                                    ?: 0)
                            viewModel.attachment = bitmap
                        }
                    } catch (e: Exception) {
                        Timber.i("File not found")
                    }
                }
            }
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap = data?.extras?.get(EXTRAS_IMG_KEY) as Bitmap
                    viewModel.attachment = bitmap
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            //            intent.resolveActivity(requireActivity().packageManager)?.also {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
//            }
        }
    }
}