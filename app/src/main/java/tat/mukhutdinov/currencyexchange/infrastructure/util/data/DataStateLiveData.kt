package tat.mukhutdinov.currencyexchange.infrastructure.util.data

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import timber.log.Timber

class DataStateLiveData<T>(
    private val isDataOneShot: Boolean = false,
    private val isLoadingOneShot: Boolean = false,
    private val isErrorOneShot: Boolean = true,
    private val isCompleteOneShot: Boolean = false
) : MutableLiveData<DataState<T>>() {

    private var state: DataState<T>? = null

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in DataState<T>>) {
        if (hasActiveObservers()) {
            Timber.w("Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner, Observer { viewState ->
            if (!isStateOneShot(viewState) || viewState !== state) {
                state = viewState
                observer.onChanged(viewState)
            } else if (viewState.state is State.Error && viewState.data != null) {
                observer.onChanged(DataState(data = viewState.data, state = State.Success))
            }
        })
    }

    fun onNext(data: T) {
        value = DataState.success(data)
    }

    fun onError(throwable: Throwable) {
        value = DataState.error(throwable, value?.data)
    }

    fun onComplete() {
        value = DataState.complete(value?.data)
    }

    fun onStart() {
        value = DataState.loading(value?.data)
    }

    private fun isStateOneShot(state: DataState<T>) =
        when (state.state) {
            State.Loading -> isLoadingOneShot
            State.Success -> isDataOneShot
            State.Complete -> isCompleteOneShot
            is State.Error -> isErrorOneShot
        }

    companion object {
        fun <T> createForSingle() = DataStateLiveData<T>(
            isDataOneShot = true,
            isCompleteOneShot = true,
            isErrorOneShot = true,
            isLoadingOneShot = true
        )
    }
}