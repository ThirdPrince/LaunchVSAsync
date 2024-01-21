package flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import utils.log

fun main() = runBlocking {
    flow<String> {
        while (true) {
            emit("发送一条弹幕")
        }
    }.sample(1000).flowOn(Dispatchers.Default).collect {
              log(it)
    }
}