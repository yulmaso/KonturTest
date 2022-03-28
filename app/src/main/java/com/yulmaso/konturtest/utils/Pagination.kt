package com.yulmaso.konturtest.utils

class Pagination(
    val limit: Int = DEFAULT_COUNT
) {
    companion object {
        const val DEFAULT_COUNT     = 50
    }

    enum class LoadType {
        REFRESH_LOAD,
        SCROLL_LOAD
    }

    var noMoreData = false
        private set
    var offset = 0
        private set

    fun startLoad(type: LoadType): Boolean {
        return when (type) {
            LoadType.REFRESH_LOAD -> {
                offset = 0
                noMoreData = false
                true
            }
            LoadType.SCROLL_LOAD -> {
                noMoreData
            }
        }
    }

    fun stopLoad(noMoreData: Boolean = false, failureOccured: Boolean = false) {
        this.noMoreData = noMoreData
        if (!failureOccured) offset += limit
    }
}