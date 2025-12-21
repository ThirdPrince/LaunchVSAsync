package async

import getUserError
import kotlinx.coroutines.*
import utils.ClientManager
import utils.log
import kotlin.system.measureTimeMillis

/**
 * async
 * 返回有结果
 */
suspend fun main() {
    //testAsyncCancel()
     log("end")
    testConcurrentAsync()
}

/**
 * async 带返回值
 */
suspend fun testAsync1() {
    val deferred = GlobalScope.async {
        delay(200)
        log("async--1")
        "jjj"
    }
    log("result-->${deferred.await()}")
}

/**
 * async 带返回值
 */
suspend fun testAsyncCancel() {
    val job = GlobalScope.async {
        delay(200)
        log("async--1")
        "jjj"
    }
    job.cancel()
    log("cancel")
}

suspend fun testErrorUseAsync() {

    val job = GlobalScope.async {
        getUserError()
    }
    try {
        job.await()
    }catch (e:Exception){
        log("e -->$e")
    }

}

/**
 * 并发获取user
 */
suspend fun testConcurrentAsync() {
    val userList = listOf(1, 2, 3, 4, 5,6,7,8,9)
    runBlocking {
        val costTime = measureTimeMillis {
            val deferred = userList.map {
                async {
                    ClientManager.getUserAsync(it)
                }
            }
            val result = deferred.awaitAll()
            log("result -->${result.toString()}")
        }
        log("costTime-->${costTime}")

    }
}

