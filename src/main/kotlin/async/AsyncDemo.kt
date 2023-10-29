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
    // testErrorUseAsync()
    testConcurrentAsync()
}

suspend fun testAsync1() {
    val deferred = GlobalScope.async {
        delay(200)
        log("async--1")
        "jjj"
    }
    log("result-->${deferred.await()}")
}

suspend fun testErrorUseAsync() {

    val job = GlobalScope.async {
        getUserError()
    }
    job.join()

}

suspend fun testConcurrentAsync() {
    val userList = listOf(1, 2, 3, 4, 5)
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

