package queue

import java.util.concurrent.PriorityBlockingQueue
import kotlin.system.measureTimeMillis

internal class Task(val priority: Int, val content: String) : Comparable<Task> {
    override fun compareTo(other: Task): Int {
        return other.priority.compareTo(this.priority) // 降序排列
    }
}

fun main() {
    val testSize = 100_000
    val threadCount = Runtime.getRuntime().availableProcessors()

    // 测试阻塞队列
    val blockingQueue = PriorityBlockingQueue<Task>(testSize, null)
    val blockingTime = measureTimeMillis {
        val threads = List(threadCount) { threadId ->
            Thread {
                try {
                    repeat(testSize / threadCount) { i ->
                        blockingQueue.put(Task(i % 10, (threadId * i).toString()))
                        blockingQueue.take()
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }.apply { start() }
        }
        threads.forEach { it.join() }
    }

    println("blockingTime =$blockingTime")
}