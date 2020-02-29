package com.pavelhabzansky.citizenapp.features.issues.detail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.pavelhabzansky.citizenapp.BR
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.ARG_ISSUE_DATA
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.fromJson
import com.pavelhabzansky.citizenapp.databinding.FragmentIssueDetailBinding
import com.pavelhabzansky.citizenapp.features.issues.detail.states.IssueDetailViewStates
import com.pavelhabzansky.citizenapp.features.issues.detail.view.vm.IssueDetailViewModel
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class IssueDetailFragment : BaseFragment() {

    private val viewModel by viewModel<IssueDetailViewModel>()

    private lateinit var binding: FragmentIssueDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_issue_detail, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val issueJson = requireNotNull(it.getString(ARG_ISSUE_DATA))
            viewModel.issue = issueJson.fromJson(IssueVO::class.java)
            binding.setVariable(BR.issue, viewModel.issue)
            binding.executePendingBindings()

            registerEvents()

            viewModel.loadIssueImage()
        }
    }

    private fun registerEvents() {
        viewModel.issueDetailViewStates.observe(this, Observer {
            updateViewState(it)
        })

        viewModel.issueDetailErrorStates.observe(this, Observer {
            Timber.w(it.t, "Unexpected error")
        })
    }

    private fun updateViewState(event: IssueDetailViewStates) {
        when (event) {
            is IssueDetailViewStates.IssueImageLoadedEvent -> {
                binding.issueImage.setImageBitmap(event.image)
            }
        }
    }

}