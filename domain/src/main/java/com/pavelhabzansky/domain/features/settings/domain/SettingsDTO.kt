package com.pavelhabzansky.domain.features.settings.domain

import com.pavelhabzansky.domain.features.issues.domain.IssueType
import com.pavelhabzansky.domain.features.places.domain.PlaceTypeDO

data class SettingsDTO(
        val placeSettings: Set<PlaceTypeDO>,
        val issueSettings: Set<IssueType>
)