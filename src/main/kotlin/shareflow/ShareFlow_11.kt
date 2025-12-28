package shareflow

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.log

fun main() = runBlocking{
    val flow = MutableSharedFlow<Int>(
        replay = 2,  // 注意：replay 值也会被丢弃！
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    
    launch {
        flow.collect{
            log("it==$it")
        }
    }

    launch {
        flow.emit(1)  // replay: [1]
        flow.emit(2)  // replay: [1, 2]
        flow.emit(3)  // 总缓冲区满: [1,2] + [3]
        flow.emit(4)  // 丢弃最旧值(1)，现在: [2,3,4]
    }
    log("end")


}