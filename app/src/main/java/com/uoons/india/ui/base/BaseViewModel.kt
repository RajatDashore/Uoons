package com.uoons.india.ui.base

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel

import io.reactivex.disposables.CompositeDisposable
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
abstract class BaseViewModel<N : Any> : ViewModel() {

    private val isLoading = ObservableBoolean(false)
    protected val disposable = CompositeDisposable()

    var navigator: N? = null

    override fun onCleared() {
        super.onCleared()
        //  Logger.e("BaseViewModel", "onCleared -> " + navigator!!.javaClass.simpleName)
        disposable.clear()
    }

    fun setIsLoading(isLoading: Boolean) {
        this.isLoading.set(isLoading)
    }

}
