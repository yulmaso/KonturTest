package com.yulmaso.konturtest.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

open class BaseViewModel(app: Application): AndroidViewModel(app) {

    private val compositeDisposable = CompositeDisposable()

    final override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    protected fun addDisposable(d: Disposable) {
        compositeDisposable.add(d)
    }
}