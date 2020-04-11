package com.pavelhabzansky.domain.features.issues.repository

import androidx.lifecycle.LiveData
import com.pavelhabzansky.domain.features.issues.domain.MapItemsDO

interface IMapItemsRepository {

    suspend fun loadMapItems(useContext: String): MapItemsDO

}