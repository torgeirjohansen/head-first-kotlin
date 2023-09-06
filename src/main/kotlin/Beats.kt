import kotlinx.coroutines.delay
import java.io.File
import javax.sound.sampled.AudioSystem

class Beats {
    fun playBeats(beats: String, file: String) {
        val parts = beats.split("x")
        var count = 0
        for (part in parts) {
            count += part.length + 1
            if (part == "") {
                playSound(file)
            } else {
                Thread.sleep(100 * (part.length + 1L))
                if (count < beats.length) {
                    playSound(file)
                }
            }
        }
    }

    suspend fun playBeatsWithSuspend(beats: String, file: String) {
        println("Playing using thread ${Thread.currentThread().name}")
        val parts = beats.split("x")
        var count = 0
        for (part in parts) {
            count += part.length + 1
            if (part == "") {
                playSound(file)
            } else {
                // A better approach in this situation is to use the coroutines delay function instead.
                // This has a similar effect to Thread.sleep, except that instead of pausing the current thread,
                // it pauses the current coroutine.

                // here, a coroutine running playBeatsWithSuspend will suspend upon executing the delay call,
                // which makes the coroutine wait for the given tme. When the delay is finished, the Kotlin scheduler
                // puts the suspended coroutine back into the thread for execution, plays the sound and
                // continues the loop or this function call

                // upon suspension, the runtime saves all the local variables used so far, saves where the code has executed
                // so far in this method, and returns the state. More on this later.
                // Finally, upon resume, the runtime restores all the local
                // variables from the saved context, jumps to where the code was suspended, and resumes execution

                // details:
                //     How is a coroutine run by the JVM ? 3 different ways:
                //    1. Event loop-based: for instance when doing runBlocking. One thread in a while true loop looks for
                //    Jobs to execute
                //
                //    2. CoroutineScheduler: default dispatcher. The dispatcher spawns a pool of worker threads with each thread attached
                //    to a designated CPU core. Each worker maintains a local job queue and in addition there is a global queue.
                //    Jobs (coroutines) submitted by a worker will be added to the worker’s local queue
                //
                //    3. ExecutorCoroutineDispatcher: wraps around a plain Java Executor. Each submitted coroutine is also wrapped as a Runnable
                //    The per-thread affinity is not preserved, because when a coroutine returns due to suspension,
                //    the next scheduling will be added to the thread pool’s job queue.
                //    And every thread has a fair chance to grab it (   most common thread pool implementations).
                delay(100 * (part.length + 1L))
                if (count < beats.length) {
                    playSound(file)
                }
            }
        }
    }

    fun playSound(file: String) {
        val clip = AudioSystem.getClip()
        val audioInputStream = AudioSystem.getAudioInputStream(File(file))
        clip.open(audioInputStream)
        clip.start()
    }
}