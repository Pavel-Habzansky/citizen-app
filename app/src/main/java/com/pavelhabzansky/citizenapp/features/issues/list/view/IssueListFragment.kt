package com.pavelhabzansky.citizenapp.features.issues.list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.databinding.FragmentIssueListBinding
import com.pavelhabzansky.citizenapp.features.issues.list.states.IssueListViewStates
import com.pavelhabzansky.citizenapp.features.issues.list.view.adapter.IssueListAdapter
import com.pavelhabzansky.citizenapp.features.issues.list.view.vm.IssueListViewModel
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class IssueListFragment : BaseFragment() {

    private lateinit var binding: FragmentIssueListBinding

    private val viewModel by viewModel<IssueListViewModel>()

    private val adapter: IssueListAdapter by lazy {
        IssueListAdapter()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIssueListBinding.inflate(inflater, container, false)

        binding.issueRecycler.setHasFixedSize(true)
        binding.issueRecycler.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        binding.issueRecycler.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()

        viewModel.loadIssues()
    }

    private fun registerEvents() {
        viewModel.issueListViewState.observe(this, Observer {
            updateViewState(it)
        })

        viewModel.issueListErrorState.observe(this, Observer {
            Timber.w(it.t, "Error occured")
        })
    }

    private fun updateViewState(event: IssueListViewStates) {
        when (event) {
            is IssueListViewStates.IssueLoadedEvent -> updateItems(event.issues)
        }
    }

    private fun updateItems(issues: List<IssueVO>) {
        // TODO Update items
    }

}