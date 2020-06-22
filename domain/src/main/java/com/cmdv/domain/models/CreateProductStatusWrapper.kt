package com.cmdv.domain.models

class CreateProductStatusWrapper<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): CreateProductStatusWrapper<T> {
            return CreateProductStatusWrapper(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): CreateProductStatusWrapper<T> {
            return CreateProductStatusWrapper(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): CreateProductStatusWrapper<T> {
            return CreateProductStatusWrapper(Status.LOADING, data, null)
        }
    }
}

enum class Status {
    LOADING,
    ERROR,
    SUCCESS

}