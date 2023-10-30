package javademo

import java.util.concurrent.CompletableFuture

fun main() {
    val startTime = System.currentTimeMillis()
    val future1 = CompletableFuture.supplyAsync {
        add(10, 20)
    }
    val future2 = CompletableFuture.supplyAsync {
        add(10, 20)
    }
    val sumFuture = future1.thenCombine(
        future2
    ) { result1: Int, result2: Int -> result1 + result2 }
    val sum = sumFuture.join()
    println("sum--->$sum")
    println("Time cost -->" + (System.currentTimeMillis() - startTime))
}

fun add(a: Int, b: Int): Int {
    try {
        Thread.sleep(200)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
    return a + b
}