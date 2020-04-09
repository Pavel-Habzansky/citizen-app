package com.pavelhabzansky.domain.core

abstract class SimpleUseCase<out T, in Params> {

    operator fun invoke(params: Params): T = doWork(params)

    protected abstract fun doWork(params: Params): T

}