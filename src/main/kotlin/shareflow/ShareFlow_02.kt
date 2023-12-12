package shareflow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking
import utils.log

val sharedFlow = MutableSharedFlow<Int>(replay = 1)
fun main() = runBlocking{

    List(10){
        delay(1000)
        sharedFlow.tryEmit(it)
    }
    val success = sharedFlow.tryEmit(1)
    if (success) {
        println("Value successfully emitted")
    } else {
        println("Collector not ready to receive value")
    }

    sharedFlow.collect{
        log(it)
    }
    log("end")

}




