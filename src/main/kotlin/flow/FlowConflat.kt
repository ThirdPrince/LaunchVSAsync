package flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import utils.log

fun main() = runBlocking{
    val flow =  flow<Int> {
        for (i in 1..5){
            delay(100)
            emit(i)
        }
    }
    flow.onEach {
        delay(50)
        log("receiver")
    }.collect()
}