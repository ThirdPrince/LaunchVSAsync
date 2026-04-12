package flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.log

fun main()  = runBlocking{
    launch {
        uiFlow().collect {
          log("it= $it")
        }
    }


    delay(2000)

    launch {
        bgFlow().collect {
            log("it= $it")
        }
    }
    log("end")

}

fun bgFlow() = testFlow().filter{
    it  == 2
}.onEach {
    log("downLoad particles")
}

fun uiFlow() = testFlow().filter{
    it ==1
}.onEach {
    log("downLoad particles")
}

fun testFlow() = flowOf(1, 2)