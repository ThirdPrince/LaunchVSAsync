package queue

import com.sample.notblcokingqueue.*
import kotlinx.coroutines.*
import utils.log
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.system.measureTimeMillis

class CoroutineBlockingQueue<T>(
    private val capacity: Int
) {

    private val queue = ConcurrentLinkedQueue<T>()

    private val waitingReceivers =
        ConcurrentLinkedQueue<Continuation<T>>()

    private val waitingSenders =
        ConcurrentLinkedQueue<Pair<T, Continuation<Unit>>>()

    private val size = AtomicInteger(0)

    suspend fun put(value: T) {

        // 先尝试直接匹配 receiver
        val receiver = waitingReceivers.poll()
        if (receiver != null) {
            receiver.resume(value)
            return
        }

        // 尝试入队
        while (true) {
            val s = size.get()

            if (s >= capacity) break

            if (size.compareAndSet(s, s + 1)) {
                queue.offer(value)
                return
            }
        }

        // 队列满 -> suspend
        return suspendCancellableCoroutine { cont ->
            waitingSenders.offer(value to cont)
        }
    }

    suspend fun take(): T {

        while (true) {

            val v = queue.poll()

            if (v != null) {

                size.decrementAndGet()

                val sender = waitingSenders.poll()

                if (sender != null) {
                    val (sv, cont) = sender
                    putInternal(sv)
                    cont.resume(Unit)
                }

                return v
            }

            val sender = waitingSenders.poll()

            if (sender != null) {
                val (sv, cont) = sender
                cont.resume(Unit)
                return sv
            }

            return suspendCancellableCoroutine { cont ->
                waitingReceivers.offer(cont)
            }
        }
    }

    private fun putInternal(value: T) {
        queue.offer(value)
        size.incrementAndGet()
    }
}
fun main() = runBlocking {
    val customQueue = CoroutineBlockingQueue<Int>(10000) // ✅ 优化6：扩大队列容量，减少 notFull 等待次数
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