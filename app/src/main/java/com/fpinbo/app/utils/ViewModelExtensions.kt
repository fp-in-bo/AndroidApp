package androidx.lifecycle

import arrow.fx.IO
import java.io.Closeable

fun <T, R> ViewModel.runIO(
    io: IO<T>,
    liveData: MutableLiveData<R>,
    errorMapping: (Throwable) -> R,
    successMapping: (T) -> R
) {
    val disposable = io.unsafeRunAsyncCancellable { result ->
        val liveDataValue = result.fold(
            ifLeft = { errorMapping(it) },
            ifRight = { successMapping(it) }
        )
        liveData.postValue(liveDataValue)
    }

    setTagIfAbsent(disposable.hashCode().toString(), object : Closeable {
        override fun close() {
            disposable()
        }
    })
}
