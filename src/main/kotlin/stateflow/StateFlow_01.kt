package stateflow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.log

fun main() = runBlocking {
    val stateFlow = MutableStateFlow(0)
    stateFlow.emit(1)
    launch {
        stateFlow.collect {
            log(it)
        }
    }

    delay(100)
    stateFlow.emit(2)
    List(10){
        delay(10)
        stateFlow.emit(it)
    }
    log("end")

}