package shareflow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import utils.log


fun main() = runBlocking {
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
    val personalisedChanges = flow {
        emit(true)

    }

    combine(permissionChanges, personalisedChanges) { permissionState, personalisedState ->
        if (personalisedState) {
            permissionState //
        } else {
            false //
        }
    }.dropWhile { it }.distinctUntilChanged().filter { it }
        .collect {
            log("State changed to: $it")
        }
}