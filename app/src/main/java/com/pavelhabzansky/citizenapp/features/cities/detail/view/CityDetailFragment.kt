package com.pavelhabzansky.citizenapp.features.cities.detail.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pavelhabzansky.citizenapp.BR
import com.pavelhabzansky.citizenapp.R
import com.pavelhabzansky.citizenapp.core.*
import com.pavelhabzansky.citizenapp.core.fragment.BaseFragment
import com.pavelhabzansky.citizenapp.core.fragment.toast
import com.pavelhabzansky.citizenapp.databinding.FragmentCityDetailBinding
import com.pavelhabzansky.citizenapp.features.cities.detail.states.CityDetailViewStates
import com.pavelhabzansky.citizenapp.features.cities.detail.view.adapter.CityGalleryAdapter
import com.pavelhabzansky.citizenapp.features.cities.detail.view.vm.CityDetailViewModel
import com.pavelhabzansky.citizenapp.features.cities.detail.view.vo.CityGalleryItemVO
import com.pavelhabzansky.citizenapp.features.cities.detail.view.vo.CityInformationVO
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class CityDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentCityDetailBinding

    private val viewModel by viewModel<CityDetailViewModel>()

    private val galleryAdapter: CityGalleryAdapter by lazy {
        CityGalleryAdapter()
    }

    private var fabOpen = false

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_city_detail, container, false)

        binding.photogalleryRecycler.setHasFixedSize(true)
        binding.photogalleryRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.photogalleryRecycler.adapter = galleryAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()

        val cityKey = arguments?.getString(ARG_CITY_KEY)
        if (cityKey != null) {
            viewModel.loadCityInfo(key = cityKey)
        } else {
            viewModel.loadResidentialCity()
        }

        binding.cityDescription.movementMethod = ScrollingMovementMethod()
        binding.mainFab.setOnClickListener { toggleFabMenu() }

        binding.newsFab.setOnClickListener { showNews() }
        binding.showOnMapFab.setOnClickListener { showOnMap() }
        binding.webPageFab.setOnClickListener { showWeb() }
        binding.setResidentialFab.setOnClickListener { setResidential() }
    }

    private fun updateViewState(state: CityDetailViewStates) {
        when (state) {
            is CityDetailViewStates.CityInformationLoaded -> setCityData(state.info)
            is CityDetailViewStates.ResidentialCityExists -> showResidenceExistsDialog(state.name)
            is CityDetailViewStates.NoResidentialCity -> showNoResidenceDialog()
            is CityDetailViewStates.SetResidential -> binding.favImg.show()
            is CityDetailViewStates.CityGalleryLoadedEvent -> updateGallery(state.gallery)
        }
    }

    private fun updateGallery(items: List<CityGalleryItemVO>) {
        galleryAdapter.updateItems(items)
    }

    private fun showNoResidenceDialog() {
        val dialog = activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.apply {
                setMessage(
                        getString(R.string.city_detail_residential_not_set_message)
                )?.setTitle(R.string.city_detail_residential_not_set_title)

                setPositiveButton(R.string.ok) { dialog, _ ->
                    dialog?.dismiss()
                    findNavController().navigateUp()
                }
            }

            builder.create()
        }
        dialog?.setCancelable(false)
        dialog?.show()
    }

    private fun showResidenceExistsDialog(name: String) {

        val dialog = activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.apply {
                setMessage(
                        getString(
                                R.string.city_detail_residential_already_set,
                                name,
                                viewModel.cityInfo.name
                        )
                )?.setTitle(R.string.warning)

                setPositiveButton(R.string.yes) { dialog, _ ->
                    dialog?.dismiss()
                    viewModel.setCityAsResidentialForce()
                }
                setNegativeButton(R.string.no) { dialog, _ -> dialog?.dismiss() }
            }

            builder.create()
        }
        dialog?.setCancelable(false)
        dialog?.show()
    }

    private fun showNews() {
        val args = Bundle().also { it.putString(ARG_KEY_NEWS_SOURCE, viewModel.cityInfo.toJson()) }
        findNavController().navigate(R.id.news_citizen_fragment, args)
        toggleFabMenu()
    }

    private fun setCityData(city: CityInformationVO) {
        binding.setVariable(BR.info, city)
        binding.executePendingBindings()

        binding.mainFab.show()

        if (!city.residential) {
            binding.favImg.hide()
        }

        val bytes = city.logoBytes
        bytes?.let {
            val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            binding.cityLogo.setImageBitmap(bmp)
        }
    }

    private fun registerEvents() {
        viewModel.cityDetailViewState.observe(this, Observer {
            updateViewState(state = it)
        })

        viewModel.cityDetailErrorState.observe(this, Observer {
            Timber.e(it.error)
        })
    }

    private fun showOnMap() {
        val lat = viewModel.cityInfo.lat
        val lng = viewModel.cityInfo.lng

        val args = Bundle()

        if (lat != null && lng != null) {
            args.putDouble(ARG_CITY_LAT, lat)
            args.putDouble(ARG_CITY_LNG, lng)
        }

        findNavController().navigate(R.id.map_fragment, args)
        toggleFabMenu()
    }

    private fun showWeb() {
        val www = viewModel.cityInfo.www
        if (www.isNotEmpty()) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(www))
            startActivity(browserIntent)
        } else {
            toast("Město nemá webové stránky")
        }
        toggleFabMenu()
    }

    private fun setResidential() {
        viewModel.setCityAsResidential()
        toggleFabMenu()
    }

    private fun toggleFabMenu() {
        if (fabOpen) {
            animateRotateClose(binding.mainFab)
            animateFade(binding.newsFab)
            animateFade(binding.setResidentialFab)
            animateFade(binding.showOnMapFab)
            animateFade(binding.webPageFab)
        } else {
            animateRotateOpen(binding.mainFab)
            animateShow(binding.newsFab)
            animateShow(binding.setResidentialFab)
            animateShow(binding.showOnMapFab)
            animateShow(binding.webPageFab)
        }
        fabOpen = !fabOpen
    }

    private fun animateFade(view: View) {
        view.animate()
                .alpha(0f)
                .setDuration(150)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        view.visibility = View.INVISIBLE
                    }
                })
    }

    private fun animateShow(view: View) {
        view.visibility = View.VISIBLE
        view.alpha = 0f
        view.animate()
                .alpha(1f)
                .setDuration(150)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        view.visibility = View.VISIBLE
                    }
                })
    }

    private fun animateRotateOpen(view: View) {
        view.animate()
                .rotation(45f)
                .setDuration(150)
    }

    private fun animateRotateClose(view: View) {
        view.animate()
                .rotation(0f)
                .setDuration(150)
    }

}