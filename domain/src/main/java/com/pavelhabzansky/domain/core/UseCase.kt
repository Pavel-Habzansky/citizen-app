package com.pavelhabzansky.domain.core

abstract class UseCase<out T, in Params> {

    suspend operator fun invoke(params: Params): T = doWork(params)

    protected abstract suspend fun doWork(params: Params): T

}