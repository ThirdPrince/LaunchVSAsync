package shareflow

import flow.Signal
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import utils.log
import java.util.concurrent.BlockingQueue
import java.util.concurrent.DelayQueue
import java.util.concurrent.Delayed
import java.util.concurrent.TimeUnit


fun main() = runBlocking {

    val flow = MutableSharedFlow<Signal>()




    List(10 * 1) {
        delay(100)
        flow.emit(Signal.CALLING)
    }
    println("Restart")

    List(10*5) {
        delay(100)
        flow.emit(Signal.SLEEPING)
    }

    delay(2000)

    List(10*5) {
        delay(100)
        flow.emit(Signal.CALLING)
    }

    println("End")

    // Allow time for the timers to run
    delay(10000)
}



