package flow

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.log

val isAvailable = MutableStateFlow(1)

fun isAvailableFlow(): Flow<Int> = isAvailable.asStateFlow()


fun main() = runBlocking {
    val flow2 = MutableStateFlow(2)
    val flow: StateFlow<Int> = combine(flow2,isAvailableFlow()) { isAvailableFlow, flow2 ->
        isAvailableFlow + flow2
    }.stateIn(
        this, started = SharingStarted.WhileSubscribed(), -1
    )
    launch {
        flow.collect {
            log("it = $it")
        }
    }

    log("end")
}