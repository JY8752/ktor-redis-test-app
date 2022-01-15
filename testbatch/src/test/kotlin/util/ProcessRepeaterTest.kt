package util

import io.kotest.core.spec.style.FunSpec
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProcessRepeaterTest : FunSpec({
    test("指定された処理を開始できること") {
        val repeater = ProcessRepeater(1) {
            delay(1 * 1000L)
        }
        val repeater2 = ProcessRepeater(2) {
            delay(2 * 1000L)
        }

        launch { repeater.start() }
        launch { repeater2.start() }

        while (!repeater.isEnd || !repeater2.isEnd) {
            delay(1 * 1000L)
        }
    }
})