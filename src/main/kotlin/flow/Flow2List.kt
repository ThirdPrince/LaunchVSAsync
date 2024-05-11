package flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun main() = runBlocking{
    val flow = flow<Int>{
        emit(1)
        delay(100)
        emit(2)
        delay(200)
    }
    val list = flow.flowOn(Dispatchers.IO).toList()
    println("list -->$list")

    println("end")
}