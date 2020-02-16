package com.pavelhabzansky.data.features.issues.mapper

import com.pavelhabzansky.data.core.mapper.MapperDirectional
import com.pavelhabzansky.data.features.issues.entities.IssueEntity
import com.pavelhabzansky.domain.features.issues.domain.IssueDO
import com.pavelhabzansky.domain.features.issues.domain.IssueType

object IssueMapper : MapperDirectional<IssueEntity, IssueDO>() {

    override fun mapFrom(from: IssueEntity) = IssueDO(
        title = from.title,
        createTime = from.createTime,
        type = IssueType.fromString(from.type.type),
        description = from.description,
        lat = from.lat,
        lng = from.lng,
        img = from.img
    )

    override fun mapTo(to: IssueDO) = IssueEntity(
        title = to.title,
        createTime = to.createTime,
        type = com.pavelhabzansky.data.features.issues.model.IssueType.fromString(to.type.type),
        description = to.description,
        lat = to.lat,
        lng = to.lng,
        img = to.img
    )

}