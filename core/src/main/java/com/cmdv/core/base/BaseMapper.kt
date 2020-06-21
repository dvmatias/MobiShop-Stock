package com.cmdv.core.base

abstract class BaseMapper<E, M> {

    abstract fun transformEntityToModel(e: E): M

    open fun transformModelToEntity(m: M): E {
        throw UnsupportedOperationException()
    }

}