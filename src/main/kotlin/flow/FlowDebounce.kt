package flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import utils.log

fun main()  = runBlocking{
    flow<Int> {
        emit(1)
        emit(2)
        delay(600)
        emit(3)
        delay(100)
        emit(4)
        delay(100)
        emit(5)

    }.debounce(500).collect{
        log(it)
    }
}