package ru.isma.javafx.extensions.coroutines

class TestUiThreadExecutor : UiThreadExecutor {
    private var pendingRunnable: (() -> Unit)? = null

    override fun executeOnUi(runnable: () -> Unit) {
        pendingRunnable = runnable
    }

    fun executePending() {
        pendingRunnable?.invoke()
        pendingRunnable = null
    }

    fun hasPending(): Boolean = pendingRunnable != null
}
