package shareflow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.log


fun main() = runBlocking {

    val flow = MutableSharedFlow<String>()

    launch {
        flow.collect {
            log("#1 flow -->$it")
        }
    }


   // flow.emit("Message--->test")
    delay(200)
    launch {
        flow.collect {
            log("#2 flow -->$it")
        }
    }

    List(10) {
        delay(100)
        flow.emit("Message--->$it")
    }
    log("restart")
    launch {
        flow.collect {
            log("#3 flow -->$it")
        }
    }

    List(10) {
        delay(100)
        flow.emit("restart Message--->$it")
    }

    log("end")
}




