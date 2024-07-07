package stateflow

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.log

fun main() = runBlocking{
    val stateFlow = MutableSharedFlow<Pair<String,Int>>(100)

    launch {
        List(100){
            stateFlow.emit((Pair(""+it,it)))
        }
    }
   launch {
       stateFlow.collect{
           log("collect -->$it")
       }
   }

    //delay(1000)
    log("end")
}