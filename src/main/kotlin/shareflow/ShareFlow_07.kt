package shareflow

import flow.Signal
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import utils.log
import java.util.concurrent.BlockingQueue
import java.util.concurrent.DelayQueue
import java.util.concurrent.Delayed
import java.util.concurrent.TimeUnit

const val CALL_THRESHOLD = 3000L
fun main() = runBlocking {

    val flow = MutableSharedFlow<Signal>()

    launch {
        var callingStartTime: Long = 0
        var hangUpStartTime: Long = 0
        flow.collect {


             if (it.name ==Signal.CALLING.name){
                 hangUpStartTime = 0
            }else{
                 if (callingStartTime != 0L) {
                     callingStartTime = 0
                     println("Call ended.")
                     // 启动挂断计时器
                     hangUpStartTime = System.currentTimeMillis()
                 }
             }

            when (it) {
                Signal.CALLING -> {
                    if (callingStartTime == 0L) {
                        callingStartTime = System.currentTimeMillis()
                        log("有电话信号-->$callingStartTime")
                    } else {
                        val elapsedTime = System.currentTimeMillis() - callingStartTime
                        if (elapsedTime >= CALL_THRESHOLD) {
                            callingStartTime = 0
                            log("你正在打电话")
                            // 在这里执行打电话的业务逻辑
                        }
                    }
                }
                Signal.SLEEPING ->{

                }
                Signal.OTHER -> {

                }
                else -> {}
            }

            if (hangUpStartTime != 0L) {
                val elapsedTime = System.currentTimeMillis() - hangUpStartTime
                if (elapsedTime >= 3000L) {
                    hangUpStartTime = 0
                    log("你挂断了电话")
                    // 在这里执行挂断逻辑
                }
            }

            }
    }

    List(10 * 5) {
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



