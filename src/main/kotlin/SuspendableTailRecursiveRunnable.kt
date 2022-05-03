class SuspendableTailRecursiveRunnable<T>(
    var accumulator: T,
    val action: (T) -> Unit,
    val condition: (T) -> Boolean
) : Runnable {

    val hasFinished: Boolean
        get() = condition(accumulator)

    override fun run() = action(accumulator)
}
