package com.pavelhabzansky.citizenapp.features.news.states

import com.pavelhabzansky.citizenapp.features.news.view.vo.NewsItemViewObject

sealed class NewsViewState {

    class NewsCacheLoadedViewState(val news: List<NewsItemViewObject>) : NewsViewState()

    class NewsLoadedViewState(val news: List<NewsItemViewObject>) : NewsViewState()

    class NewsItemLoadedViewState(val item: NewsItemViewObject) : NewsViewState()

    class NoResidentialViewState : NewsViewState()

}

sealed class NewsErrorState(val t: Throwable) {



}