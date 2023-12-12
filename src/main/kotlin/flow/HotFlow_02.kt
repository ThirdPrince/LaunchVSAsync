package flow

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import shareflow.sharedFlow
import utils.log


fun main() = runBlocking {

    val numbersHotFlow = flow<Int> {
         List(5){
             emit(it)
         }
    }.shareIn(GlobalScope, started = SharingStarted.WhileSubscribed())
    launch {
        numbersHotFlow.collect { println("1st Collector: $it") }
    }


    launch {
        numbersHotFlow.collect { println("2nd Collector: $it") }
    }

    delay(100)
//    List(10){
//        //delay(100)
//        numbersHotFlow.emit(it)
//    }

    log("end")

}



