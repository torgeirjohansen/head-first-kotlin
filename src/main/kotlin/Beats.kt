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
                delay(100 * (part.length + 1L))
                if (count < beats.length) {
                    playSound(file)
                }
            }
        }
    }

    fun playSound(file: String) {
        val clip = AudioSystem.getClip()
        val audioInputStream= AudioSystem.getAudioInputStream(File(file))
        clip.open(audioInputStream)
        clip.start()
    }
}