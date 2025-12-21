package task

import kotlinx.coroutines.*
import utils.log
import java.util.concurrent.Executors

var executor = Executors.newFixedThreadPool(2)
fun main() = runBlocking {
    val scope = CoroutineScope(executor.asCoroutineDispatcher())
    scope.launch {
        log("大任务开始")
        val task = List(100) { index ->
            launch {
                log("小任务 $index 开始")
                doWork(index)
                log("小任务 $index 完成")
            }
        }
        task.joinAll()
    }.join()

    log("end")
}

// 模拟小任务的工作
suspend fun doWork(index: Int) {
    delay(1000L)  // 模拟耗时操作
    println("小任务 $index 正在工作")
}