package shareflow

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.log

fun main() = runBlocking{
    val flow = MutableSharedFlow<Int>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    launch {
        flow.collect{
            delay(1000)
            log("it ==$it")
        }
    }
    launch {
        flow.emit(1)  // 成功
        flow.emit(2)  // 成功
        flow.emit(3)  // 挂起，直到有空间！
    }

    launch {
        delay(5000)
        val result1 = flow.tryEmit(1)  // true，缓冲区：[1]
        val result2 = flow.tryEmit(2)  // false，缓冲区已满
        val result3 = flow.tryEmit(3)  // false，缓冲区已满
    }



    log("end")

}