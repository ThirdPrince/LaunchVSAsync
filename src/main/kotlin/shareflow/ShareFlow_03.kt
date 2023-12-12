package shareflow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.log


fun main() = runBlocking{

   val flow = MutableSharedFlow<String>(replay = 10)
    flow.tryEmit("Message1")
    launch {
        flow.sample(1000)
            .collect{
            log(it)
        }
    }

    flow.tryEmit("Message2")

    delay(2000)

    flow.tryEmit("Message3")

    delay(100)
    List(10){
        flow.tryEmit("Message--->$it")
    }

    log("end")

}




