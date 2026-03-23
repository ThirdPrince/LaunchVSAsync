package com.sample.notblcokingqueue

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import utils.log
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.LinkedList
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.resume
import kotlin.system.measureTimeMillis

val executorForCouroutine = java.util.concurrent.Executors.newFixedThreadPool(64)

val itemCount = 100000
val concurrency = 64
val POISON_PILL = Int.MIN_VALUE

class NotBlockingPriorityQueue(private val capacity: Int) {
    private val queue = LinkedList<Int>()

    // ✅ 优化1：读写分离，生产者和消费者不再互相阻塞
    private val headMutex = Mutex()  // take 专用锁
    private val tailMutex = Mutex()  // put 专用锁

    private val notEmpty = Condition(headMutex)
    private val notFull = Condition(tailMutex)

    // ✅ 优化2：原子变量维护 size，避免加锁读长度
    private val size = AtomicInteger(0)

    suspend fun put(item: Int) {
        val shouldSignal: Boolean
        tailMutex.withLock {
            while (size.get() == capacity) {
                notFull.await()
            }
            queue.addLast(item)
            // ✅ 优化3：只有从空变非空时才需要通知消费者
            shouldSignal = size.getAndIncrement() == 0
        }
        // ✅ 优化4：signal 移到锁外，减少锁竞争
        if (shouldSignal) notEmpty.signal()
    }

    suspend fun take(): Int {
        val shouldSignal: Boolean
        val item: Int
        headMutex.withLock {
            while (size.get() == 0) {
                notEmpty.await()
            }
            item = queue.pollFirst()
            // ✅ 优化3：只有从满变非满时才需要通知生产者
            shouldSignal = size.getAndDecrement() == capacity
        }
        // ✅ 优化4：signal 移到锁外
        if (shouldSignal) notFull.signal()
        return item
    }
}

class Condition(private val mutex: Mutex) {
    private val waiters = LinkedList<CancellableContinuation<Unit>>()
    // ✅ 优化5：计数器避免无效 signal
    private val waiterCount = AtomicInteger(0)

    suspend fun await() {
        waiterCount.incrementAndGet()
        suspendCancellableCoroutine<Unit> { cont ->
            waiters.add(cont)
            cont.invokeOnCancellation {
                waiters.remove(cont)
                waiterCount.decrementAndGet()
            }
            mutex.unlock()
        }
        waiterCount.decrementAndGet()
        mutex.lock()
    }

    fun signal() {
        if (waiterCount.get() > 0) {
            waiters.poll()?.resume(Unit)
        }
    }

    fun signalAll() {
        if (waiterCount.get() > 0) {
            while (waiters.isNotEmpty()) {
                waiters.poll()?.resume(Unit)
            }
        }
    }
}

fun main() = runBlocking {
    val customQueue = NotBlockingPriorityQueue(10000) // ✅ 优化6：扩大队列容量，减少 notFull 等待次数
    val consumedCount = AtomicInteger(0)

    val time = measureTimeMillis {
        launch(executorForCouroutine.asCoroutineDispatcher()) {
            for (i in 1..itemCount) {
                customQueue.put(i)
            }
            repeat(concurrency) {
                customQueue.put(POISON_PILL)
            }
        }

        val jobs = List(concurrency) {
            launch(executorForCouroutine.asCoroutineDispatcher()) {
                while (true) {
                    val data = customQueue.take()
                    if (data == POISON_PILL) break
                    consumedCount.incrementAndGet()
                }
            }
        }
        jobs.joinAll()
    }

    log("Custom SimpleQueue: 处理 $itemCount 条数据\n总耗时: $time ms")
}