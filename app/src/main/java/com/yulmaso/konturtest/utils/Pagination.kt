package com.yulmaso.konturtest.utils

class Pagination(
    val limit: Int = DEFAULT_COUNT
) {
    companion object {
        const val DEFAULT_COUNT     = 50
    }

    enum class LoadType {
        FIRST_LOAD,
        REFRESH_LOAD,
        SCROLL_LOAD,
        SEARCH_LOAD
    }

    var noMoreData = false
        private set
    var offset = 0
        private set

    var loadType = LoadType.FIRST_LOAD
        private set
    var searchInput: String = ""
        private set
    var failureOccured = false
        private set

    fun startLoad(type: LoadType, searchInput: String?): Pagination = synchronized(this) {
        this.loadType = type
        this.searchInput = searchInput ?: ""
        if (type != LoadType.SCROLL_LOAD) {
            offset = 0
            noMoreData = false
        }
        failureOccured = false
        return this
    }

    fun stopLoad(itemCount: Int? = null, failureOccured: Boolean = false) = synchronized(this) {
        itemCount?.let { this.noMoreData = itemCount < limit - 1}
        if (!failureOccured) offset += limit
        this.failureOccured = failureOccured
    }
}