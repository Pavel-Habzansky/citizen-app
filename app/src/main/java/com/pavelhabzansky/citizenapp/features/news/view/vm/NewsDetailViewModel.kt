package com.pavelhabzansky.citizenapp.features.news.view.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.pavelhabzansky.citizenapp.core.model.SingleLiveEvent
import com.pavelhabzansky.citizenapp.core.vm.BaseViewModel
import com.pavelhabzansky.citizenapp.features.news.states.NewsErrorState
import com.pavelhabzansky.citizenapp.features.news.states.NewsViewState
import com.pavelhabzansky.citizenapp.features.news.view.mapper.NewsVOMapper
import com.pavelhabzansky.domain.features.news.domain.NewsDO
import com.pavelhabzansky.domain.features.news.usecase.LoadNewsItemUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.inject

class NewsDetailViewModel : BaseViewModel() {

    private val loadNewsItemUseCase by inject<LoadNewsItemUseCase>()

    private var news: LiveData<NewsDO>? = null

    val newsDetailViewState = SingleLiveEvent<NewsViewState>()
    val newsErrorViewState = SingleLiveEvent<NewsErrorState>()

    private val newsObserver: Observer<NewsDO> by lazy {
        Observer<NewsDO> {
            newsDetailViewState.postValue(
                NewsViewState.NewsItemLoadedViewState(
                    NewsVOMapper.mapFrom(
                        from = it
                    )
                )
            )
        }
    }

    fun loadItem(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            news = loadNewsItemUseCase(params = LoadNewsItemUseCase.Params(title = title))
            launch(Dispatchers.Main) { news?.observeForever(newsObserver) }
        }
    }

    override fun onCleared() {
        super.onCleared()

        news?.removeObserver(newsObserver)
    }

}