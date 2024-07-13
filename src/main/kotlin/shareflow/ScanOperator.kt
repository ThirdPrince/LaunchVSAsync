package shareflow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun main()  = runBlocking{
    val permissionChanges = flow {
        emit(true)
        delay(100)
        emit(false)
        delay(100)
        emit(true)
        delay(100)
        emit(false)
        delay(1000)
        emit(true)
        emit(false)
        delay(1000)
        emit(true)
        delay(1000)
        emit(false)

    }

    permissionChanges.dropWhile { it }
        .scan(false to true) { previousPair, current ->
            previousPair.second to current
        }
        .filter { !it.first && it.second }
        .map { it.second }
        .collect { value -> println("State changed to: $value") }

}