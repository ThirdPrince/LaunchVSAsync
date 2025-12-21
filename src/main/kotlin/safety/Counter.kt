package safety

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock

fun main() = runBlocking {
    val counter = Counter()
    val jobs = List(20) {
        launch(Dispatchers.IO) {
            repeat(100) {
                counter.decrement()  // 并发修改 count
            }

        }
    }

    // 等待所有协程完成
    jobs.forEach { it.join() }

    // 打印最终结果
    println("Final count: ${counter.getCount()}")
}

class Counter {
    private val atomicInteger = AtomicInteger(1000)
    suspend fun decrement() {
        if (atomicInteger.get() > 0) {
            delay(10)
            atomicInteger.decrementAndGet()
        }


    }

    suspend fun getCount(): Int {
        delay(10)
        return atomicInteger.get()
    }
}