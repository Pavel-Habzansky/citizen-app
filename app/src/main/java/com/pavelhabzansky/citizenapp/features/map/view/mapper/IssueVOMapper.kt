package com.pavelhabzansky.citizenapp.features.map.view.mapper

import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueVO
import com.pavelhabzansky.data.core.mapper.MapperDirectional
import com.pavelhabzansky.domain.features.issues.domain.IssueDO

object IssueVOMapper : MapperDirectional<IssueVO, IssueDO>() {

    override fun mapFrom(from: IssueVO) = IssueDO(
        title = from.title,
        createTime = from.createTime,
        type = IssueTypeVOMapper.mapFrom(from = from.type),
        description = from.description,
        lat = from.lat,
        lng = from.lng,
        img = from.img
    )

    override fun mapTo(to: IssueDO) = IssueVO(
        title = to.title,
        createTime = to.createTime,
        type = IssueTypeVOMapper.mapTo(to = to.type),
        description = to.description,
        lat = to.lat,
        lng = to.lng,
        img = to.img
    )

}
