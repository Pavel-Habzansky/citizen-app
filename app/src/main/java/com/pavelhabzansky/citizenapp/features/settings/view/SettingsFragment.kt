package com.pavelhabzansky.citizenapp.features.settings.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.*
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.fragment.findParentNavController
import com.pavelhabzansky.citizenapp.databinding.FragmentSettingsBinding
import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueTypeVO
import com.pavelhabzansky.citizenapp.features.place.view.vo.PlaceTypeVO
import com.pavelhabzansky.citizenapp.features.settings.states.SettingsViewStates
import com.pavelhabzansky.citizenapp.features.settings.view.adapter.IssueSettingsAdapter
import com.pavelhabzansky.citizenapp.features.settings.view.adapter.PlaceSettingsAdapter
import com.pavelhabzansky.citizenapp.features.settings.view.vm.SettingsViewModel
import org.koin.android.ext.android.bind
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

class SettingsFragment : BaseFragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val viewModel by viewModel<SettingsViewModel>()

    private val placeSettingsAdapter: PlaceSettingsAdapter by lazy {
        PlaceSettingsAdapter(onCheck = this::onPlaceSettingCheck)
    }

    private val issueSettingsAdapter: IssueSettingsAdapter by lazy {
        IssueSettingsAdapter(onCheck = this::onIssueSettingCheck)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        binding.closeButton.setOnClickListener { findParentNavController().navigateUp() }
        binding.saveButton.setOnClickListener { saveCurrentSettings() }
        binding.allCheckPlaces.setOnCheckedChangeListener { _, isChecked -> viewModel.setAllPlaces(isChecked) }
        binding.allCheckIssues.setOnCheckedChangeListener { _, isChecked -> viewModel.setAllIssues(isChecked) }

        binding.placesSettingsRecycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = this@SettingsFragment.placeSettingsAdapter
        }

        binding.issuesSettingsRecycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = this@SettingsFragment.issueSettingsAdapter
        }

        arguments?.let {
            viewModel.useContext = it.getString(USE_CONTEXT_ARG, USE_CONTEXT_EMPTY)
            when {
                isCitizenContext() -> {
                    binding.issueSettingsContainer.show()
                    binding.placeSettingsContainer.hide()
                }
                isTouristContext() -> {
                    binding.issueSettingsContainer.hide()
                    binding.placeSettingsContainer.show()
                }
                isEmptyContext() -> {
                    binding.issueSettingsContainer.show()
                    binding.placeSettingsContainer.show()

                    initUserPrefControls()
                }
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.settings).isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()

        viewModel.loadAllSettings()
    }

    private fun registerEvents() {
        viewModel.settingsViewStates.observe(this, Observer {
            updateViewStates(it)
        })

        viewModel.settingsErrorStates.observe(this, Observer {
            Timber.e(it.t, "Error occured")
        })
    }

    private fun updateViewStates(event: SettingsViewStates) {
        when (event) {
            is SettingsViewStates.SettingsLoadedEvent -> updateSettings(event.placeTypes, event.issueTypes)
            is SettingsViewStates.PlaceSettingsLoadedEvent -> updatePlaceSettings(event.placeTypes)
            is SettingsViewStates.IssueSettingsLoadedEvent -> updateIssueSettings(event.issueTypes)
            is SettingsViewStates.SettingsSavedEvent -> findParentNavController().navigateUp()
        }
    }

    private fun initUserPrefControls() {
        binding.userPreferencesContainer.show()

        val prefs = context?.getSharedPreferences(USER_PREF_SPACE, Context.MODE_PRIVATE)
        prefs?.let {
            binding.langSwitch.isChecked = when (it.getString(LANG_PREF_KEY, LANG_CS)) {
                LANG_CS -> false
                else -> true
            }

            binding.langSwitch.setOnCheckedChangeListener { _, isChecked ->
                val selectedLang = if (isChecked) {
                    LANG_EN
                } else {
                    LANG_CS
                }

                prefs.edit { putString(LANG_PREF_KEY, selectedLang) }

                val newLocale = Locale(selectedLang)
                val newConfig = Configuration(resources.configuration)
                Locale.setDefault(newLocale)
                newConfig.setLocale(newLocale)

                activity?.baseContext?.resources?.updateConfiguration(newConfig, resources.displayMetrics)
                activity?.baseContext?.applicationContext?.resources?.updateConfiguration(newConfig, resources.displayMetrics)
                activity?.recreate()
            }
        }
    }


    private fun updateSettings(placeSettings: Set<PlaceTypeVO>, issueSettings: Set<IssueTypeVO>) {
        updatePlaceSettings(placeSettings)
        updateIssueSettings(issueSettings)
    }

    private fun updatePlaceSettings(settings: Set<PlaceTypeVO>) {
        if (settings.size == PlaceTypeVO.values().size) {
            binding.allCheckPlaces.isChecked = true
        }
        placeSettingsAdapter.setSettings(settings)
    }

    private fun updateIssueSettings(settings: Set<IssueTypeVO>) {
        if (settings.size == IssueTypeVO.values().size) {
            binding.allCheckIssues.isChecked = true
        }
        issueSettingsAdapter.setSettings(settings)
    }

    private fun saveCurrentSettings() {
        viewModel.saveSettings()
    }

    private fun onPlaceSettingCheck(type: PlaceTypeVO, checked: Boolean) {
        viewModel.changePlaceSettings(type, checked)
    }

    private fun onIssueSettingCheck(type: IssueTypeVO, checked: Boolean) {
        viewModel.changeIssueSettings(type, checked)
    }

    private fun isCitizenContext() = viewModel.useContext == USE_CONTEXT_CITIZEN

    private fun isTouristContext() = viewModel.useContext == USE_CONTEXT_TOURIST

    private fun isEmptyContext() = viewModel.useContext == USE_CONTEXT_EMPTY

}