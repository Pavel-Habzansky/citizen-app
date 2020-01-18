package com.pavelhabzansky.data.core.mapper

abstract class MapperDirectional<FROM, TO> {

    abstract fun mapFrom(from: FROM): TO

    abstract fun mapTo(to: TO): FROM

}

abstract class Mapper<FROM, TO> {

    abstract fun mapFrom(from: FROM): TO

}