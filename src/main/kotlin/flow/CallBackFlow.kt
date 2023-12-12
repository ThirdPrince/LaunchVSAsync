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
fun fetchDataAsFlow(): Flow<User> = callbackFlow {
    repeat(3) { it ->
        ClientManager.getUser(it){ user->
          //  log("user-->$user")
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
    val flow = fetchDataAsFlow().shareIn(GlobalScope, started = SharingStarted.WhileSubscribed())
    launch {
        flow.collect { data ->
            println("#1 Received data: $data")
        }
    }


    delay(1500)
    //println("#2 Received data:start")
    launch {
        flow.collect { data ->
            println("#2 Received data: $data")
        }
    }


    // 等待一些时间，以便看到清理操作的输出
    delay(2000)
}
