package com.baseprojectmvvmkotlin.data.model

class Event<T>(private val content: T) {

    var isAlreadyHandled = false

    fun getContent(): T? {
        if (!isAlreadyHandled) {
            isAlreadyHandled = true
            return content
        }
        return null
    }

    fun peekContent(): T {
        return content
    }
}
