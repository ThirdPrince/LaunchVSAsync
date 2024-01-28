package flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import utils.log

fun main() = runBlocking {
    flow<String> {
        var count = 0
        while (true) {
            delay(100)
            emit("发送一条弹幕-->${count++}")
        }
    }.sample(1000).flowOn(Dispatchers.Default).collect {
              log(it)
    }
}