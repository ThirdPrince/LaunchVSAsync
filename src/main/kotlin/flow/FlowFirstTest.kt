package flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.log

private val _state = MutableSharedFlow<Int>(replay = 1)
val state: Flow<Int> = _state
fun main() = runBlocking {

    launch {
        _state.emit(2)
     }

    println(state.first())
    delay(2)
    launch {
        state.collect{
            log("it==$it")
        }
    }
    println(state.first())
}

