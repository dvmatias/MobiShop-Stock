package com.cmdv.domain.models

class ProductCreationStatusModel<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): ProductCreationStatusModel<T> {
            return ProductCreationStatusModel(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): ProductCreationStatusModel<T> {
            return ProductCreationStatusModel(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): ProductCreationStatusModel<T> {
            return ProductCreationStatusModel(Status.LOADING, data, null)
        }
    }
}

enum class Status {
    LOADING,
    ERROR,
    SUCCESS

}