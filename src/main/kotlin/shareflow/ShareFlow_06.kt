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

    launch {
        var callingDetected = false
        var callingStartTime: Long = 0
        var processingCallLogic = false

        flow.collect {
                if (it == Signal.CALLING) {
                    if (!callingDetected) {
                        callingDetected = true
                        callingStartTime = System.currentTimeMillis()
                        log("有电话信号")
                        processingCallLogic = false // 重置标志，确保只输出一次
                    } else {
                        val elapsedTime = System.currentTimeMillis() - callingStartTime
                        if (elapsedTime >= 3000 && !processingCallLogic) {
                            processingCallLogic = true
                            log("正在打电话")
                            // 在这里执行打电话的业务逻辑
                        }
                    }
                } else {
                    if (callingDetected) {
                        callingDetected = false
                        log("电话挂断了")
                        processingCallLogic = false // 重置标志，以便在再次检测到CALLING时输出
                    }
                }
            }
    }

    List(10 * 5) {
        delay(100)
        flow.emit(Signal.CALLING)
    }
    log("Restart")

    List(10*2) {
        delay(100)
        flow.emit(Signal.OTHER)
    }

    delay(2000)

    List(10*5) {
        delay(100)
        flow.emit(Signal.CALLING)
    }


    List(10*2) {
        delay(100)
        flow.emit(Signal.OTHER)
    }
    log("End")

    // 等待一段时间，以触发3秒以上的CALLING
    delay(50000)
}



data class DelayedElement(val value: String, val delayMillis: Long) : Delayed {
    private val expirationTime: Long = System.currentTimeMillis() + delayMillis

    override fun getDelay(unit: TimeUnit): Long {
        val diff = expirationTime - System.currentTimeMillis()
        return unit.convert(diff, TimeUnit.MILLISECONDS)
    }

    override fun compareTo(other: Delayed): Int {
        return getDelay(TimeUnit.MILLISECONDS).compareTo(other.getDelay(TimeUnit.MILLISECONDS))
    }
}

object DelayQueueManager {
    private val delayQueue: BlockingQueue<DelayedElement> = DelayQueue()
    private var callingStartTime: Long = 0
    private val callingThreshold = 3000L // 3 seconds

    init {
        startTimer(1000 * 3)
    }

    fun addElement(value: String, delayMillis: Long) {
        delayQueue.clear()
        val delayedElement = DelayedElement(value, delayMillis)
        delayQueue.offer(delayedElement)
    }

    fun remove() {
        delayQueue.clear()
    }

    fun takeNext(): String {
        val delayedElement = delayQueue.take()
        return delayedElement.value
    }

    private fun startTimer(checkIntervalMillis: Long) {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                checkQueue()
                delay(checkIntervalMillis)
            }
        }
    }

    private fun checkQueue() {
        val element = delayQueue.poll()
        if (element != null) {
            if (element.value == Signal.CALLING.name) {
                if (callingStartTime == 0L) {
                    callingStartTime = System.currentTimeMillis()
                } else {
                    val elapsedMillis = System.currentTimeMillis() - callingStartTime
                    if (elapsedMillis >= callingThreshold) {
                        println("Continuous calling for $elapsedMillis ms. Processing call logic.")
                        // 在这里执行打电话的业务逻辑
                    }
                }
            } else {
                callingStartTime = 0L // Reset the timer when non-CALLING signal is received
            }
        } else {
            callingStartTime = 0L // Reset the timer when the queue is empty
        }
    }
}


