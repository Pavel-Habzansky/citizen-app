package com.pavelhabzansky.citizenapp.features.news.states

import android.graphics.Bitmap
import com.pavelhabzansky.citizenapp.features.news.view.vo.NewsItemViewObject
import com.pavelhabzansky.citizenapp.features.news.view.vo.ScheduleViewObject

sealed class NewsViewState {

    class NewsCacheLoadedViewState(val news: List<NewsItemViewObject>) : NewsViewState()

    class NewsLoadedViewState(val news: List<NewsItemViewObject>) : NewsViewState()

    class NewsItemLoadedViewState(val item: NewsItemViewObject) : NewsViewState()

    class LocationPermissionGranted : NewsViewState()

    class LocationPermissionNotGranted : NewsViewState()

    class NoConnectionEvent() : NewsViewState()

    class SchedulesLoadedEvent(val schedules: List<ScheduleViewObject>) : NewsViewState()

    class EventsDownloadComplete : NewsViewState()

}

sealed class NewsErrorState(val t: Throwable) {

    class UnexpectedErrorEvent(val e: Exception) : NewsErrorState(e)

}