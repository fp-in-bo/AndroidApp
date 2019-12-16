package androidx.lifecycle

import arrow.core.Either
import arrow.fx.IO
import arrow.fx.OnCancel
import java.io.Closeable


fun <T> IO<T>.unsafeRunAsyncInViewModel(
    viewModel: ViewModel,
    onCancel: OnCancel = OnCancel.Silent,
    cb: (Either<Throwable, T>) -> Unit
) {
    val disposable = unsafeRunAsyncCancellable(onCancel, cb)

    viewModel.setTagIfAbsent(disposable.hashCode().toString(), object : Closeable {
        override fun close() {
            disposable()
        }
    })
}
