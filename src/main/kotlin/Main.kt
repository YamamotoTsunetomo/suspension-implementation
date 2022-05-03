import java.util.*

fun main() {

    val runnableOne = SuspendableTailRecursiveRunnable(accumulator = mutableListOf(),
        action = fun(l: MutableList<Int>) {
            if (l.isEmpty())
                l.add(1)
            else
                l.add(l[l.size - 1] + 1)
            println("${Thread.currentThread().name}     $l")
        },
        condition = fun(l: MutableList<Int>): Boolean {
            return l.size == 7
        })
    val runnableTwo = SuspendableTailRecursiveRunnable(accumulator = mutableListOf(),
        action = fun(l: MutableList<Int>) {
            if (l.isEmpty())
                l.add(10)
            else
                l.add(l[l.size - 1] + 1)
            println("${Thread.currentThread().name}     $l")
        },
        condition = fun(l: MutableList<Int>): Boolean = l.size == 3)
    val stack = Stack<SuspendableTailRecursiveRunnable<MutableList<Int>>>()
    stack.add(runnableOne)
    val suspendableThread = SuspendableThread(stack)


    var i = 0
    suspendableThread.startWithSuspendingCondition {
        if (++i > 3)
            it.sus()
    }


    suspendableThread.addToStack(runnableTwo)
    suspendableThread.res()

}