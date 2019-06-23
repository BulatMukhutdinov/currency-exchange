package tat.mukhutdinov.currencyexchange.infrastructure.util.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import tat.mukhutdinov.currencyexchange.infrastructure.util.data.DataState
import timber.log.Timber

fun <DataType> LiveData<DataState<DataType>>.observeViewState(
    owner: LifecycleOwner,
    dataCallback: ((DataType) -> Unit)? = null,
    errorCallback: ((Throwable) -> Unit)? = null,
    completeCallback: (() -> Unit)? = null,
    loadingCallback: ((Boolean) -> Unit)? = null
) {
    this.observe(owner, Observer { dataState ->
        dataState?.either(dataCallback, errorCallback, completeCallback, loadingCallback)
    })
}

fun Context.toast(text: String): Toast = Toast
    .makeText(this, text, Toast.LENGTH_LONG)
    .apply { show() }

fun ViewModel.getExceptionHandler(onException: (throwable: Throwable) -> Unit) = CoroutineExceptionHandler { _, throwable ->
    viewModelScope.launch {
        onException(throwable)
    }

    Timber.e(throwable)
}