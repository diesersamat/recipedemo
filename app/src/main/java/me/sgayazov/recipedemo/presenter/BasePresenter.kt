package me.sgayazov.recipedemo.presenter

import io.reactivex.disposables.Disposable
import me.sgayazov.recipedemo.dataprovider.Interactor

abstract class BasePresenter {
    var interactor: Interactor = Interactor()
    private val listOfDisposable: MutableList<Disposable> = mutableListOf()

    fun onStop() {
        listOfDisposable.forEach({ it.dispose() })
    }

    protected fun addSubscription(disposable: Disposable) {
        listOfDisposable.add(disposable)
    }
}