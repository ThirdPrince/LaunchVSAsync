package launch

import getUserError
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.channelFlow
import utils.ClientManager
import utils.User
import utils.log
import kotlin.system.measureTimeMillis

/**
 * launch
 * 返回无结果
 */
 suspend fun main() {

    //testLaunch1()
   // testLaunchCancel()
    (1..5).asFlow()
        .collect {
            log(it)
        }
    //testConcurrentLaunch()

    channelFlow {
        (0..10).forEach {
            trySend(it)
        }
    }.collect {
        log(it)
    }

}

suspend fun testLaunch1() {
    val job = GlobalScope.launch {
        log(delay(200))
        log("launch--1")
    }
    job.join()
}

suspend fun testLaunchCancel() {
    delay(100)
    val job = GlobalScope.launch {
        log(delay(200))
        log("launch--1")
    }
    job.cancel()
    log("cancel")
}

suspend fun testError() {
    val job = GlobalScope.launch {
        try {
            getUserError()
        } catch (e: Exception) {
            log("error->${e.message}")
        }
    }
    job.join()

}

suspend fun testConcurrentLaunch() {
    val userList = listOf(1, 2, 3, 4, 5)
    val users = mutableListOf<User>()
    runBlocking {
        val costTime = measureTimeMillis {
            val jobs = userList.map {
                launch {
                    val user = ClientManager.getUserAsync(it)
                    users.add(user)
                }
            }
            jobs.forEach {
                it.join()
            }
            log("result -->${users}")
        }
        log("costTime-->${costTime}")

    }
}

