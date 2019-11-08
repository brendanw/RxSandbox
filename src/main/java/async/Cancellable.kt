package async

interface Cancellable {
    val isCancelled: Boolean
    fun cancel(): Boolean
}