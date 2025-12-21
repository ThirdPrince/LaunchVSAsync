package sample

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap


var values = ConcurrentHashMap<String, Int>()
val mutex = Mutex()
fun main()= runBlocking{

    val job1 = launch {
        save("北大")
    }
    val job2 = launch {
        save("清华")
    }
    val job3 = launch {
        save("清华")
    }
    job1.join()
    job2.join()
    job3.join()
    println(values.toString())
}


suspend fun save(account: String) = withContext(Dispatchers.IO) {

    Thread.sleep(3000)
    mutex.withLock {
        if (values.containsKey(account)) {
            values[account] = values[account]!! + 1 // 使用 !! 断言非空
        } else {
            values[account] = 1
        }
    }


}