package flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import utils.log

fun main() = runBlocking {
    val flow = simpleFlow()
    log("Calling collect")

    flow.collect{
        log("coldFlow -->1-->$it")
    }

    delay(2000)
    log("Calling collect again")
    flow.collect {
        log("coldFlow -->2--->$it")
    }

    log("end")


}

fun simpleFlow(): Flow<Int> = flow {
    for (i in 1..10) {
        delay(1000)
        //log("emit-->$i")
        emit(i)
    }
}