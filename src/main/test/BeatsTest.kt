import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

class BeatsTest {

    /* Corutines

      https://kotlinlang.org/docs/multiplatform-mobile-concurrency-and-coroutines.html#coroutines
      https://kotlinlang.org/docs/coroutines-overview.html#how-to-start

      A coroutine is an instance of suspendable computation. It is conceptually similar to a thread,
      in the sense that it takes a block of code to run that works concurrently with the rest of the code.
      However, a coroutine is not bound to any particular thread. It may suspend its execution in one
      thread and resume in another one

      Compared to a Java thread, a Kotlin coroutine has a smaller memory footprint and lower overhead in context-switching.

      For us programmers, the Kotlin runtime scheduling hides the complexities of managing concurrent coroutines

      Replacing “suspended” with “blocked” and “coroutine” with “thread” points to an obvious analogy in that coroutines
      and threads both enter waiting states and can resume executing after transitioning out of waiting states

      Coroutines, at the end of the day, are just a bunch of jobs getting scheduled and executed at some point in JVM.
      As long as we keep that in mind, from a programming model perspective it’s very similar to thread programming. We
      will get back to the details ...
     */

    @Test
    fun `should play beats in sequence`() {
        val beats = Beats()
        beats.playBeats("x-x-x-x-x-x-", TOM_FILE)
        beats.playBeats("x-----x----", CYMBAL_FILE)
    }

    @Test
    fun `should play beats in parallel`() {
        val beats = Beats()
        // Runs a new coroutine and blocks the current thread until its completion.
        //  use when you need to bridge regular and suspending code in synchronous way,
        //  by blocking the thread.
        runBlocking {

            // launch is a coroutine builder. It launches a new coroutine concurrently 
            // with the rest of the code, which continues to work independently
            launch { beats.playBeatsWithSuspend("x-x-x-x-x-x-", TOM_FILE) }
            beats.playBeatsWithSuspend("x-----x----", CYMBAL_FILE)
        }
        System.out.println("Main thread continues")
    }

    @Test
    fun `should play several beats in parallel using custom dispatcher with 1 thread`() {
        val beats = Beats()
        runBlocking(Executors.newFixedThreadPool(1).asCoroutineDispatcher()) {
            launch { suspendable(beats) }
            launch { beats.playBeatsWithSuspend("x---x---x---x---x---x---x---x---x---x---x---x-", HIGH_HAT) }
            launch { beats.playBeatsWithSuspend("-x-------x-------x-------x-------x-------x---", CYMBAL_FILE) }
        }
    }

    @Test
    fun `should play beats in parallel in without blocking scope`() {
        println("Main thread: ${Thread.currentThread().name}")
        val beats = Beats()
        val tomJob = GlobalScope.launch { beats.playBeatsWithSuspend("x-x-x-x-x-x-", TOM_FILE) }
        val cymbalJob = GlobalScope.launch { beats.playBeatsWithSuspend("x-----x----", CYMBAL_FILE) }

        // Really, REALLY fire and forget, think of them as daemon threads.
        tomJob.start()
        cymbalJob.start()
    }

    @Test
    fun `should play beats in parallel in without blocking scope with sleep `() {
        println("Main thread: ${Thread.currentThread().name}")
        val beats = Beats()
        val tomJob = GlobalScope.async { beats.playBeatsWithSuspend("x-x-x-x-x-x-", TOM_FILE) }
        val cymbalJob = GlobalScope.launch { beats.playBeatsWithSuspend("x-----x----", CYMBAL_FILE) }

        tomJob.start()
        cymbalJob.start()
        Thread.sleep(2000)
    }

    @Test
    fun `should play several beats in parallel`() {
        val beats = Beats()
        // We can execute as many blocking operations as we want.
        // Under the hood, those operations will be handled by a fixed number of threads without
        // excessive thread creation

        // Remember that the default coroutine scope will run with only 1 thread!

        // runBlocking{} also starts a new coroutine but blocks the current thread: main for the
        // duration of the call until all the code inside the runBlocking{} function body complete their execution.
        runBlocking {
            launch { beats.playBeatsWithSuspend("----x-------x-------x-------x-------x-------", SNARE) }
            launch { beats.playBeatsWithSuspend("x---x---x---x---x---x---x---x---x---x---x---x-", HIGH_HAT) }
            launch { beats.playBeatsWithSuspend("-x-------x-------x-------x-------x-------x---", CYMBAL_FILE) }
        }
    }

    // Suspending functions are at the center of everything . A suspending function is simply a function
    // that can be paused and resumed at a later time.
    // They can execute a long running operation and wait for it to complete without blocking
    //
    // Details:
    // Under the hood, suspend functions are converted by the compiler to another function without the suspend keyword obviously,
    // that takes an addition parameter of type Continuation<T>. The function here for example, will be converted by the compiler to this
    // (Taken from decompiling the kotlin bytecode to Java: )
    // ```   private final Object suspendable(Beats beats, Continuation $completion) {```
    // Continuation<T> is an interface that contains two functions that are invoked to resume the coroutine with a return value or with an
    // exception if an error had occurred while the function was suspended.

    // Coroutines have their runtime data structures represented as Jobs along with contexts, which are just a bunch of
    // JVM objects which are OO pointers.
    // no matter how the runtime handles corutines (remember event based, CoroutineScheduler, exector), the state of the coroutine
    // is *frozen*, and if state has changed, the runner will fail (investigate further)
    private suspend fun suspendable(beats: Beats) {
        beats.playBeatsWithSuspend("----x-------x-------x-------x-------x-------", SNARE)
    }

    /**Coroutines allow multitasking without multithreading, but they don't disallow multithreading.
     *
     * In languages that support both, a coroutine that is put to sleep can be re-awakened in a different thread.
     * The usual arrangement for CPU-bound tasks is to have a thread pool with about twice as many threads as you have CPU cores.
     * This thread pool is then used to execute maybe thousands of coroutines simultaneously. The threads share a queue
     * of coroutines ready to execute, and whenever a thread's current coroutine blocks, it just gets another one to work on from the queue.
     *
     * In this situation you have enough busy threads to keep your CPU busy, and you still have thread context switches,
     * but not enough of them to waste significant resources. The number of coroutine context switches is thousands of times higher.
     *
     * */
    @Test
    fun `should play several beats in parallel using custom dispatcher with several threads`() {
        val beats = Beats()
        // There are many ways to schedule a coroutine. Some common ways include default scheduling,
        // which uses the CoroutineScheduler, and ExecutorCoroutineDispatcher, which wraps around a Java SDK Executor thread pool:
        runBlocking(Executors.newFixedThreadPool(4).asCoroutineDispatcher()) {
            launch { beats.playBeatsWithSuspend("----x-------x-------x-------x-------x-------", SNARE) }
            launch { beats.playBeatsWithSuspend("x---x---x---x---x---x---x---x---x---x---x---x-", HIGH_HAT) }
            launch { beats.playBeatsWithSuspend("-x-------x-------x-------x-------x-------x---", CYMBAL_FILE) }
        }
    }

    @Test
    fun `should play randomly created music`() {
        val beats = Beats()
        runBlocking {
            launch { beats.playBeatsWithSuspend(beats.makeBeatOfLength(60), KICK_DRUM) }
            launch { beats.playBeatsWithSuspend(beats.makeBeatOfLength(60), SNARE) }
            launch { beats.playBeatsWithSuspend(beats.makeBeatOfLength(60), HIGH_HAT) }
            launch { beats.playBeatsWithSuspend(beats.makeBeatOfLength(60), CYMBAL_FILE) }
            launch { beats.playBeatsWithSuspend(beats.makeBeatOfLength(60), FLOOR_TOM_FILE) }
            launch { beats.playBeatsWithSuspend(beats.makeBeatOfLength(60), TOM_FILE) }
        }
    }

    @Test
    fun `should stop kick drum when drummer leg is stopped`() {
        val beats = Beats()
        // If we are not interested in the result from the (long running) async computation anymore
        // we can cancel it using `cancel()`
        var leg: Job
        runBlocking {
            leg = launch { beats.playBeatsWithSuspend(beats.makeBeatOfLength(60), KICK_DRUM) }
            launch { beats.playBeatsWithSuspend(beats.makeBeatOfLength(60), SNARE) }
            delay(2000)
            leg.cancel()
        }
    }


    // Manually suspend
    @Test
    fun `what is printed 1`() {
        runBlocking {
            // async starts a new coroutine and allows you to return a result with a suspend function called await
            async {
                System.out.println("1")
                delay(200)
                System.out.println("2")
            }
            async {
                System.out.println("3")
                delay(200)
                System.out.println("4")
            }
        }
    }

    @Test
    fun `what is printed 2`() {
        runBlocking {
            async {
                System.out.println("1")
                System.out.println("2")
            }
            async {
                System.out.println("3")
                System.out.println("4")
            }
        }
    }

    @Test
    fun `what is printed 3`() {
        runBlocking {
            async {
                System.out.println("1")
                yield()
                System.out.println("2")
            }
            async {
                System.out.println("3")
                yield()
                System.out.println("4")
            }
        }
    }

    // What is yield ?
    // Yields the thread (or thread pool) of the current coroutine dispatcher
    // to other coroutines to run if possible.

    @Test
    fun `what is printed 4`() {
        runBlocking {
            println("1")
            launch {
                repeat(3) {
                    println("2")
                }
            }
            println("3")
        }
    }

    @Test
    fun `what is printed 4-1`() {
        runBlocking {
            println("1")
            launch {
                repeat(3) {
                    println("2")
                }
            }
            yield()
            println("3")
        }
    }

    @Test
    fun `what is printed 5`() {
        runBlocking {
            println("1")
            launch {
                repeat(3) {
                    println("2")
                    yield()
                }
            }
            yield()
            println("3")
        }
    }

    // The runBlocking's yield is to allow the launch to start
    // The yield in launch is to allow runBlocking retain the control to complete its function.

    @Test
    fun `what is printed 6`() {
        runBlocking {
            println("1")
            launch(Dispatchers.IO) {
                repeat(3) {
                    println("2")
                }
            }
            yield()
            println("3")
        }
    }

    // Here we have changed the coroutine context to a context that runs with several threads,
    // so only one yield in the runBlocking scope is needed.

    fun Beats.makeBeatOfLength(length: Int): String {
        val beatOrWait = Pair('x', '-');
        val beat = StringBuilder()
        (1..length).map {
            if ((0..1).random() == 0)
                beat.append(beatOrWait.first)
            else beat.append(beatOrWait.second)
        }
        return beat.toString();
    }
}
