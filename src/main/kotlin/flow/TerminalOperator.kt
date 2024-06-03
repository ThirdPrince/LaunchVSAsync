package flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import utils.log

fun main() = runBlocking{
    collectTest()
    collectLatestTest()
    toList()
    getFirst()
    single()
}
suspend fun collectTest() {
    flowOf(1,2,3).collect{
        log("collectTest-->$it")
    }
}

suspend fun collectLatestTest() {
    flow{
        emit(1)
        delay(100)
        emit(2)
    }.collectLatest{
        log("collectLatestTest-->$it")
        delay(150)
        log("$it -->done")
    }
}

suspend fun toList() {
    val list = flow{
        emit(1)
        delay(100)
        emit(2)
        delay(100)
        emit(3)
    }.toList()
    log("toList $list")
}

suspend fun getFirst() {
    val list = flow{
        emit(1)
        delay(100)
        emit(2)
        delay(100)
        emit(3)
    }.first()
    log("getFirst $list")
}

suspend fun single() {
    val list = flow{
        emit(1)
        delay(100)
        emit(2)
        delay(100)
        emit(3)
    }.single()
    log("single $list")
}