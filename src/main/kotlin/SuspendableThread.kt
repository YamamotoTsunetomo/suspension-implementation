import java.util.*

class SuspendableThread<T>(
    private val runnables: Stack<SuspendableTailRecursiveRunnable<T>>
) {
    private var thread: Thread? = null
    private var isSuspended = false
    private var threadBehaviour: () -> Unit = fun() = defaultBehaviour()

    private var suspendingCondition: (SuspendableThread<*>) -> Unit = fun(t: SuspendableThread<*>) = Unit

    fun sus() {
        isSuspended = true
        setThreadBehaviour {}
        setSuspendingCondition {}
    }

    @Synchronized
    fun setThreadBehaviour(function: () -> Unit) {
        threadBehaviour = function
    }

    @Synchronized
    fun addToStack(runnable: SuspendableTailRecursiveRunnable<T>) {
        while (!isSuspended)
            Thread.sleep(1000)

        setThreadBehaviour {}
        runnables.add(runnable)
    }

    @Synchronized
    fun setSuspendingCondition(function: (SuspendableThread<*>) -> Unit) {
//        while (!isSuspended)
//            Thread.sleep(1000)

        suspendingCondition = function
    }


    fun res() {
        isSuspended = false
        setSuspendingCondition {}
        setThreadBehaviour { defaultBehaviour() }
    }

    private fun defaultBehaviour() {
        while (runnables.isNotEmpty() && !isSuspended) {
            suspendingCondition(this)
            action()
        }
    }

    fun startWithSuspendingCondition(x: (SuspendableThread<*>) -> Unit) {
        println("startWithSuspendingCondition")
        setSuspendingCondition(x)
        if (thread == null)
            thread = Thread {
                threadBehaviour()
            }
        thread!!.start()
    }

    private fun action() {
        val current = runnables.peek()
        when {
            current.hasFinished -> runnables.pop()
            !isSuspended -> current.run()
        }
        Thread.sleep(1000)
    }

}