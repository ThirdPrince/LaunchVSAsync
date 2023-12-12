package flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import shareflow.sharedFlow
import utils.log


fun main() = runBlocking {

    val numbersHotFlow = getHotFlow()
    launch {
        numbersHotFlow.collect { println("1st Collector: $it") }
    }


    launch {
        numbersHotFlow.collect { println("2nd Collector: $it") }
    }

    delay(100)
    List(10){
        //delay(100)
        numbersHotFlow.emit(it)
    }

    log("end")

}

suspend fun getHotFlow(): MutableSharedFlow<Int> {
    val sharedFlow = MutableSharedFlow<Int>()
    (1..5).forEach {
        delay(1000)
        sharedFlow.emit(it)
    }
    return sharedFlow
}


