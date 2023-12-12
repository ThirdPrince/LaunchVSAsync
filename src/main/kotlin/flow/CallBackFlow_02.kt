package flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import utils.ClientManager
import utils.User
import utils.log

// 模拟异步操作的接口


// 实现一个简单的异步操作接口


// 使用 callbackFlow 创建 Flow
fun fetchDataAsFlow2(): Flow<User> = callbackFlow {
    repeat(2) { it ->
        delay(1000)
        ClientManager.getUser(it){ user->
            trySend(user)
        }

        delay(2000)
    }

    // 当 Flow 被取消时执行清理操作
    awaitClose {
        // 在这里可以执行一些清理操作，比如取消异步任务
        println("Flow is closed, cleaning up...")
    }
}

fun main() = runBlocking {
   // val asyncApi = SimpleAsyncApi()

    // 使用 Flow 收集数据
    val sharedFlow = MutableSharedFlow<User>()

    val coldFlow = fetchDataAsFlow2()

    // Use conflate to convert the Flow to MutableSharedFlow
    coldFlow
        .conflate()
        .onEach {
            sharedFlow.emit(it)
        }.launchIn(GlobalScope) // Launch the collection in a separate coroutine

    launch {
        coldFlow.collect{
            log("#1--->$it")
        }
    }

    // 等待一些时间，以便看到清理操作的输出
    delay(200)

    launch {
        coldFlow.collect{
            log("#2--->$it")
        }
    }

    log("end")


}
