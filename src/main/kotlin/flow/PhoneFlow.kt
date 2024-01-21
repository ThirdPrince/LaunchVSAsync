package flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.log

fun main() = runBlocking {
    val signalFlow: Flow<Signal> = flow {
        // 模拟信号的发出
        repeat(5) {
            emit(Signal.SLEEPING  )
            delay(3000) // 假设每个信号之间有3秒的间隔
        }

        repeat(3) {
            emit(Signal.OTHER)
            delay(3000)
        }

        emit(Signal.CALLING)
        delay(3000)

        emit(Signal.SLEEPING)
        delay(3000)

        emit(Signal.SLEEPING)
    }.flowOn(Dispatchers.IO)


    launch {
        signalFlow
            .debounce(10000) // 等待10秒，如果在这段时间内没有新信号发生，则执行下一步
            .collect {
                println(it)
                when (it) {
                    Signal.CALLING -> {
                        println("Calling for 10 seconds without interruption. Processing call logic.")
                        // 在这里执行打电话的业务逻辑
                    }
                    else -> {
                        println("Signal other than calling received.")
                    }
                }
            }
    }
    log("end")

}

enum class Signal{
    CALLING,
    SLEEPING,
    OTHER
}