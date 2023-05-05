import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

class BeatsTest {

    @Test
    fun `should play beats in sequence`() {
        val beats = Beats()
        beats.playBeats("x-x-x-x-x-x-", TOM_FILE)
        beats.playBeats("x-----x----", CYMBAL_FILE)
    }

    @Test
    fun `should play beats in parallel`() {
        val beats = Beats()
        runBlocking {
            launch { beats.playBeatsWithSuspend("x-x-x-x-x-x-", TOM_FILE) }
            beats.playBeatsWithSuspend("x-----x----", CYMBAL_FILE)
        }
    }


    @Test
    fun `should play beats in parallel in without blocking scope`() {
        println("Main thread: ${Thread.currentThread().name}")
        val beats = Beats()
        val job = GlobalScope.launch { beats.playBeatsWithSuspend("x-x-x-x-x-x-", TOM_FILE) }

        // Really, REALLY fire and forget
        job.start()
        Thread.sleep(2000)
    }

    @Test
    fun `should play several beats in parallel`() {
        val beats = Beats()
        // We can execute as many blocking operations as we want.
        // Under the hood, those operations will be handled by a fixed number of threads without
        // excessive thread creation

        // runBlocking{} also starts a new coroutine but blocks the current thread: main for the
        // duration of the call until all the code inside the runBlocking{} function body complete their execution.
        runBlocking {
            launch { beats.playBeatsWithSuspend("----x-------x-------x-------x-------x-------", SNARE) }
            launch { beats.playBeatsWithSuspend("x---x---x---x---x---x---x---x---x---x---x---x-", HIGH_HAT) }
            launch { beats.playBeatsWithSuspend("-x-------x-------x-------x-------x-------x---", CYMBAL_FILE) }
        }
    }

    @Test
    fun `should play several beats in parallel using custom dispatcher with 1 thread`() {
        val beats = Beats()
        runBlocking(Executors.newFixedThreadPool(2).asCoroutineDispatcher()) {
            launch { extracted(beats) }
            launch { beats.playBeatsWithSuspend("x---x---x---x---x---x---x---x---x---x---x---x-", HIGH_HAT) }
            launch { beats.playBeatsWithSuspend("-x-------x-------x-------x-------x-------x---", CYMBAL_FILE) }
        }
    }

    // Under the hood, suspend functions are converted by the compiler to another function without the suspend keyword,
    // that takes an addition parameter of type Continuation<T>. The function here for example, will be converted by the compiler to this
    // (Taken from decompiling the kotlin bytecode to Java: )
    // ```   private final Object extracted(Beats beats, Continuation $completion) {```
    // Continuation<T> is an interface that contains two functions that are invoked to resume the coroutine with a return value or with an
    // exception if an error had occurred while the function was suspended.
    private suspend fun extracted(beats: Beats) {
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

    @Test
    fun `should stop music when timeout expires`() {
        val beats = Beats()
        runBlocking {
            withTimeout(5000) {
                launch { beats.playBeatsWithSuspend(beats.makeBeatOfLength(600), KICK_DRUM) }
                launch { beats.playBeatsWithSuspend(beats.makeBeatOfLength(600), SNARE) }
            }
        }
    }

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
