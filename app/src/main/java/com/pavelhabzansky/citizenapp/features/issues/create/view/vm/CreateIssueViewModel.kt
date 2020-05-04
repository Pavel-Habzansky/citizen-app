package com.pavelhabzansky.citizenapp.features.issues.create.view.vm

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseAndroidViewModel
import com.pavelhabzansky.citizenapp.features.issues.create.states.CreateIssueErrorStates
import com.pavelhabzansky.citizenapp.features.issues.create.states.CreateIssueViewStates
import com.pavelhabzansky.citizenapp.features.map.view.mapper.IssueVOMapper
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO
import com.pavelhabzansky.data.features.issues.model.Gps
import com.pavelhabzansky.domain.features.issues.usecase.CreateIssueUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class CreateIssueViewModel(app: Application) : BaseAndroidViewModel(app) {

    private val createIssueUseCase by inject<CreateIssueUseCase>()

    val createIssueViewState = SingleLiveEvent<CreateIssueViewStates>()
    val createIssueErrorState = SingleLiveEvent<CreateIssueErrorStates>()

    lateinit var position: Gps
    var attachment: Bitmap? = null
        set(value) {
            field = value
            createIssueViewState.postValue(CreateIssueViewStates.AttachmentSetEvent(field))
        }

    fun requestCameraPermission() {
        if (hasPermission(Manifest.permission.CAMERA)) {
            createIssueViewState.postValue(CreateIssueViewStates.CameraPermissionGranted())
        } else {
            createIssueViewState.postValue(CreateIssueViewStates.CameraPermissionNotGranted())
        }
    }

    fun createIssue(issue: IssueVO) {
        viewModelScope.launch(Dispatchers.IO) {
            val issueDom = IssueVOMapper.mapFrom(from = issue)
            createIssueUseCase(CreateIssueUseCase.Params(issueDom, requireNotNull(attachment)))
            createIssueViewState.postValue(CreateIssueViewStates.IssueCreated())
        }
    }

}