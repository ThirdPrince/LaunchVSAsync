package flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import utils.log

fun main() = runBlocking {
    val result = flow<String> {
        for (i in ('A'..'Z')){
            emit(i.toString())
        }
    }.fold("Alphabet:"){
        acc, value ->  acc + value
    }
    log(result)
}