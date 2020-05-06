package com.pavelhabzansky.citizenapp.features.issues.myissues.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.hide
import com.pavelhabzansky.citizenapp.core.show
import com.pavelhabzansky.citizenapp.databinding.FragmentMyIssuesBinding
import com.pavelhabzansky.citizenapp.features.issues.myissues.state.MyIssuesViewStates
import com.pavelhabzansky.citizenapp.features.issues.myissues.view.adapter.MyIssuesAdapter
import com.pavelhabzansky.citizenapp.features.issues.myissues.view.vm.MyIssuesViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class MyIssuesFragment : BaseFragment() {

    private lateinit var binding: FragmentMyIssuesBinding

    private val viewModel by viewModel<MyIssuesViewModel>()

    private val issuesAdapter: MyIssuesAdapter by lazy {
        MyIssuesAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_issues, container, false)

        binding.issuesRecycler.setHasFixedSize(true)
        binding.issuesRecycler.layoutManager = LinearLayoutManager(context)
        binding.issuesRecycler.adapter = issuesAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerEvents()

        viewModel.loadMyIssues()
    }

    private fun registerEvents() {
        viewModel.myIssuesViewState.observe(this, Observer {
            updateViewState(it)
        })

        viewModel.myIssuesErrorState.observe(this, Observer {
            Timber.w(it.t, "Error occured")
        })
    }

    private fun updateViewState(event: MyIssuesViewStates) {
        when (event) {
            is MyIssuesViewStates.UserIssuesLoadedEvent -> {
                if (event.items.isEmpty()) {
                    binding.noIssuesCreated.show()
                    binding.issuesRecycler.hide()
                    binding.issueCounter.hide()
                } else {
                    binding.noIssuesCreated.hide()
                    binding.issuesRecycler.show()
                    binding.issueCounter.show()
                    binding.issueCounter.text = getString(R.string.user_issue_counter, event.items.size)
                    issuesAdapter.updateItems(event.items)
                }
            }
        }
    }

}