package flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.runBlocking
import utils.log

fun main()  = runBlocking{
    val result = flow<Int> {
        for (i in (1..100)){
            emit(i)
        }
    }.reduce{
        accumulator, value ->  accumulator + value
    }
    log(result)
}