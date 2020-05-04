package com.pavelhabzansky.citizenapp.features.issues.myissues.view.mapper

import android.graphics.BitmapFactory
import com.pavelhabzansky.citizenapp.features.issues.myissues.view.vo.MyIssueVO
import com.pavelhabzansky.data.core.mapper.Mapper
import com.pavelhabzansky.domain.features.issues.domain.MyIssueDO

object UserIssueMapper : Mapper<MyIssueDO, MyIssueVO>() {

    override fun mapFrom(from: MyIssueDO) = MyIssueVO(
            key = from.key,
            title = from.title,
            date = from.date,
            description = from.description,
            image = BitmapFactory.decodeByteArray(from.image, 0, from.image.size)
    )

}