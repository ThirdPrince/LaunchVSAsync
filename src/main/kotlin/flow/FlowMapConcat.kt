package flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import utils.log

fun main() = runBlocking {
    flowOf(1, 2, 3)
        .flatMapConcat {
            flowOf("as $it", "b$it")
        }.collect {
            log(it)
        }
}