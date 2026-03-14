package flow

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    launch(start = CoroutineStart.UNDISPATCHED){
        println("A")
        delay(100)
        println("B")
    }
    println("C")
}

