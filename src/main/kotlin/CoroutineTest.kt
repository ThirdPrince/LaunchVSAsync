import kotlinx.coroutines.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

fun main() = runBlocking {
    // multipleTaskUseJuc()
    //multipleTaskUseCoroutineScope()
    //multipleTaskUseSupervisorScope()
    asyncTaskUseSupervisorScope()

}


fun multipleTaskUseJuc() {
    val latch = CountDownLatch(5) // 初始化计数器为 3
    val hasFailed = AtomicBoolean(false) // 标志是否有任务失败，默认为 false

    val taskStart = System.currentTimeMillis()
    for (i in 1..5) {
        doSomethingAsync(i, object : AsyncCallback {
            override fun onSuccess(result: String) {
                println(result)
                latch.countDown() // 减少计数
                checkForFailure()
            }

            override fun onFailure(error: Throwable) {
                println("Task failed: ${error.message}")
                latch.countDown() // 减少计数
                hasFailed.set(true) // 标记任务失败
                checkForFailure()
            }

            fun checkForFailure() {
                if (hasFailed.get()) {
                    println("Task failed: One or more tasks failed")
                    // 取消其他任务
                    for (i in 0 until latch.count) {
                        latch.countDown()
                    }
                } else if (latch.count == 0L) {
                    println("All tasks completed successfully")
                }
            }
        })
    }
    latch.await() // 等待所有任务完成或有任务失败
    println("end--> cost:::${System.currentTimeMillis() - taskStart}")
}

suspend fun multipleTaskUseCoroutineScope() {
    println("multipleTaskUseCoroutine start")
    val taskStart = System.currentTimeMillis()
    val taskId = mutableListOf(1, 2, 3, 4, 5)
    try {
        coroutineScope {
            val deferredList = taskId.map {
                launch {
                    try {
                        println(doSomeAsync(it))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
//            deferredList.forEach {
//                println(it.await())
//            }
        }

    } catch (e: Exception) {
        e.printStackTrace()
        println("multipleTaskUseCoroutine failed:::${System.currentTimeMillis() - taskStart}")
    }
    println("multipleTaskUseCoroutine end--> cost:::${System.currentTimeMillis() - taskStart}")
}

suspend fun multipleTaskUseSupervisorScope() {
    println("multipleTaskUseSupervisorScope start")
    val taskStart = System.currentTimeMillis()
    val taskId = mutableListOf(1, 2, 3, 4, 5)
    try {
        supervisorScope {
            val deferredList = taskId.map {
                async {
                    println(doSomeAsync(it))
                }
            }
            deferredList.forEach {
                println(it.await())
            }
        }

    } catch (e: Exception) {
        e.printStackTrace()
        println("multipleTaskUseSupervisorScope failed:::${System.currentTimeMillis() - taskStart}")
    }
    println("multipleTaskUseSupervisorScope end--> cost:::${System.currentTimeMillis() - taskStart}")
}

suspend fun asyncTaskUseSupervisorScope() {
    println("multipleTaskUseSupervisorScope start")
    val taskStart = System.currentTimeMillis()
    val taskId = mutableListOf(1, 2, 3, 4, 5)
    try {
        coroutineScope {

            val task1 = async {
                doSomeAsync(1)
            }
            val task2 = async {
                doSomeAsync(2)
            }
            try {
                println(task1?.await())
            } catch (e: Exception) {

            }

            println(task2.await())
        }

    } catch (e: Exception) {
        e.printStackTrace()
        println("multipleTaskUseSupervisorScope failed:::${System.currentTimeMillis() - taskStart}")
    }
    println("multipleTaskUseSupervisorScope end--> cost:::${System.currentTimeMillis() - taskStart}")
}

suspend fun asyncTaskUseSupervisorScope2() {
    println("multipleTaskUseSupervisorScope start")
    val taskStart = System.currentTimeMillis()
    val taskId = mutableListOf(1, 2, 3, 4, 5)
    try {
        supervisorScope {
            val task1 = async {
                doSomeAsync2(1)
            }
            val task2 = async {
                doSomeAsync2(2)
            }
            println(task1.await())
            println(task2.await())
        }

    } catch (e: Exception) {
        e.printStackTrace()
        println("multipleTaskUseSupervisorScope failed:::${System.currentTimeMillis() - taskStart}")
    }
    println("multipleTaskUseSupervisorScope end--> cost:::${System.currentTimeMillis() - taskStart}")
}

interface AsyncCallback {
    fun onSuccess(result: String)
    fun onFailure(error: Throwable)
}

fun doSomethingAsync(taskId: Int, callback: AsyncCallback) {
    // 模拟异步操作
    Thread {
        try {
            Thread.sleep((2000 * taskId).toLong()) // 模拟耗时操作
            if (taskId == 1) {
                throw Exception("Task $taskId failed")
            }
            callback.onSuccess("Task $taskId -->completed successfully")
        } catch (e: InterruptedException) {
            callback.onFailure(e)
        } catch (e: Exception) {
            callback.onFailure(e)
        }
    }.start()
}

suspend fun doSomeAsync(task: Int) = suspendCancellableCoroutine<String> { continuation ->
    doSomethingAsync(task, object : AsyncCallback {
        override fun onSuccess(result: String) {
            continuation.resume(result)
        }

        override fun onFailure(error: Throwable) {
            continuation.resumeWithException(error)
        }

    })
}

suspend fun doSomeAsync2(task: Int): String {
    delay(task.toLong() * 1000)
    if (task == 1) {
        throw Exception("Task $task failed")
    }
    return "Task $task -->completed successfully"

}


