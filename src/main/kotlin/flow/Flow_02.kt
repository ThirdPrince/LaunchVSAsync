package flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.log

fun main() = runBlocking {
    var flow = simpleFlow3()
    log("Calling collect")

    launch {
        flow.collect{
            log("coldFlow -->1-->$it")
        }
    }


    delay(2000)
    log("Calling collect again")
    launch {
        flow.collect {
            log("coldFlow -->2--->$it")
        }
    }


     flow = simpleFlow4()
    launch {
        flow.collect {
            log("coldFlow -->3--->$it")
        }
    }
    log("end")


}

fun simpleFlow3(): Flow<Int> = flow {
    for (i in 1..10) {
        delay(1000)
        //log("emit-->$i")
        emit(i)
    }
}

fun simpleFlow4(): Flow<Int> = flow {
    for (i in 1..10) {
        delay(1000)
        //log("emit-->$i")
        emit(i)
    }
}