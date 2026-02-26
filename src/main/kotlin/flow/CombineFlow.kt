package flow

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.log

val isAvailable = MutableStateFlow(1)

fun isAvailableFlow(): Flow<Int> = flow {
    suspend { }
}


fun main() = runBlocking {
    val flow2 = MutableStateFlow(2)
    val uiState: StateFlow<Int> = combine(flow2, isAvailableFlow()) { isAvailableFlow, flow2 ->
        isAvailableFlow + flow2
    }.stateIn(
        this, started = SharingStarted.WhileSubscribed(), -1
    )
    launch {
        uiState.collect {
            log("it = $it")
        }
    }
    log("end")
}