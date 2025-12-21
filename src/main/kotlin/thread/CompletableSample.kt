package thread

import feed.compressImage
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

fun main() {
    val executor = Executors.newFixedThreadPool(20)
    val completableFuture = CompletableFuture.supplyAsync({ compressImage("1") }, executor)
    println("------")
    completableFuture.join()
    println("COMPRESS FINISHED")

}