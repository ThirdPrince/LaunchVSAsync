package async

import coroutinescope
import kotlinx.coroutines.*

fun main() = runBlocking {
   // coroutineScopeTest()
    supervisorScopeTest()
}

suspend fun coroutineScopeTest() {
    try {
        coroutineScope {
            val task1 = async {
                throw Exception("子协程失败")
            }
            val task2 = async {
                delay(1000)
                println("因为子协程失败，这段代码不会被打印")
            }
            task1.await()
            task2.await()

        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    println("end")
}

suspend fun supervisorScopeTest() {
    try {
        supervisorScope {
            val task1 = async {
                throw Exception("子协程失败")
            }
            val task2 = async {
                delay(1000)
                println("因为子协程失败，这段代码会被打印")
            }

//            task1.await()
//            task2.await()

        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    println("end")
}