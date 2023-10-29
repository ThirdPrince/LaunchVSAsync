package utils

import kotlinx.coroutines.asCoroutineDispatcher
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * 模拟客户端请求
 */
object ClientManager {

    var executor: Executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2)
    val customDispatchers = executor.asCoroutineDispatcher()

    /**
     * getUser
     */
    private fun getUser(userId: Int, callback: (User) -> Unit) {
        executor.execute {
            val sleepTime = Random().nextInt(500)
            Thread.sleep(sleepTime.toLong())
            callback(User(userId, sleepTime.toString(), "$userId-->avatar", ""))
        }
    }

    /**
     * getAvatar
     */
    fun getUserAvatar(user: User, callback: (User) -> Unit) {
        executor.execute {
            val sleepTime = Random().nextInt(1000)
            try {
                Thread.sleep(sleepTime.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            user.file = "$sleepTime.png"
            callback(user)
        }
    }

    /**
     * 异步同步化
     */
    suspend fun getUserAsync(userId: Int): User = suspendCoroutine { continuation ->
        getUser(userId) {
            continuation.resume(it)
        }
    }
}