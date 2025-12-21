package shareflow

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import utils.log

fun main() = runBlocking{
    val shareFlow = MutableSharedFlow<String>(replay = 1)
    shareFlow.emit("1")
    shareFlow.emit("2")
    shareFlow.collect{
        log("it = $it")
    }
    log("end")
}