import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.lang.StringBuilder
import kotlin.random.Random

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
    fun `should play several beats in parallel`() {
        val beats = Beats()
        runBlocking {
            launch { beats.playBeatsWithSuspend("x---x-x-x-x-x-x---x-x-x-x-x-x-x---x-x-x-x-x-x-x-", KICK_DRUM) }
            launch { beats.playBeatsWithSuspend("----x-------x-------x-------x-------x-------", SNARE) }
            launch { beats.playBeatsWithSuspend("----x-------x-------x-------x-------x-------", SNARE) }
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