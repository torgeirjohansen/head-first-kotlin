import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

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
}