package com.pavelhabzansky.citizenapp.features.map.view.mapper

import com.pavelhabzansky.citizenapp.features.map.view.vo.IssueTypeVO
import com.pavelhabzansky.data.core.mapper.MapperDirectional
import com.pavelhabzansky.domain.features.issues.domain.IssueType

object IssueTypeVOMapper : MapperDirectional<IssueTypeVO, IssueType>() {

    override fun mapFrom(from: IssueTypeVO): IssueType {
        return when(from) {
            IssueTypeVO.PUBLIC_DAMAGE -> IssueType.PUBLIC_DAMAGE
            IssueTypeVO.LOST_ITEM -> IssueType.LOST_ITEM
            else -> IssueType.UNKNOWN
        }
    }

    override fun mapTo(to: IssueType): IssueTypeVO {
        return when(to) {
            IssueType.PUBLIC_DAMAGE -> IssueTypeVO.PUBLIC_DAMAGE
            IssueType.LOST_ITEM -> IssueTypeVO.LOST_ITEM
            else -> IssueTypeVO.OTHERS
        }
    }


}