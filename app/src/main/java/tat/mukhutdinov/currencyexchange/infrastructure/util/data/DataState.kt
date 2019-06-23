package tat.mukhutdinov.currencyexchange.infrastructure.util.data

data class DataState<out T>(
    val state: State,
    val data: T?
) {
    /**
     * Common case of consuming [DataState].
     *
     * Callbacks behavior:
     * [dataCallback] is called when [data] is not `null` and [State] is not [State.Loading].
     * [loadingCallback] is called with `true` on [State.Loading] and with `false` on other Statees.
     * [errorCallback] is called on [State.Error].
     * [completeCallback] is called on [State.Complete]
     *
     * I.throwable. when [DataState] has [State.Error] and it's [data] is not `null`, then
     * [errorCallback] and [dataCallback] will be called.
     */
    fun either(
        dataCallback: ((T) -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null,
        completeCallback: (() -> Unit)? = null,
        loadingCallback: ((Boolean) -> Unit)? = null
    ) {
        when (state) {
            State.Success -> {
                loadingCallback?.invoke(false)
                data?.let { dataCallback?.invoke(it) }
            }
            is State.Complete -> {
                loadingCallback?.invoke(false)
                completeCallback?.invoke()
            }
            State.Loading -> {
                loadingCallback?.invoke(true)
            }
            is State.Error -> {
                loadingCallback?.invoke(false)
                data?.let { dataCallback?.invoke(it) }
                errorCallback?.invoke(state.throwable)
            }
        }
    }

    companion object {

        fun <T> loading(data: T? = null): DataState<T> = DataState(State.Loading, data)

        fun <T> success(data: T?): DataState<T> = DataState(State.Success, data)

        fun <T> error(e: Throwable, data: T? = null): DataState<T> = DataState(State.Error(e), data)

        fun <T> complete(data: T? = null): DataState<T> = DataState(State.Complete, data)
    }
}