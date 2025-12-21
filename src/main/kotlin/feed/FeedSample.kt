package feed

import kotlinx.coroutines.*
import utils.ClientManager
import java.sql.SQLOutput
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.system.measureTimeMillis

val executor = Executors.newFixedThreadPool(20)
val images = listOf("1", "2", "3", "4", "5")
fun main() = runBlocking{

    traditional()
    //completableFutureApproach(images, executor)

    delay(2000)

    val costTime3 = measureTimeMillis {
       coroutines()

    }

    println("costTime3-->$costTime3")

}

fun traditional() {
    val startTime = System.currentTimeMillis()
    val countDownLatch = CountDownLatch(images.size)
    val results = ConcurrentHashMap<String, String>()

    images.forEach {
        executor.execute {
            val imageCompress = compressImage(it)
            results[it] = imageCompress
            countDownLatch.countDown()
        }
    }
    countDownLatch.await()
    println("压缩完成")

    val countDownLatch2 = CountDownLatch(results.size)
    images.forEach { image ->
        executor.execute {
            uploadImage(results[image]!!)
            countDownLatch2.countDown()
        }
    }
    countDownLatch2.await()
    println("上传完成")
    executor.execute {
        submit("早安")
        println("发布成功")
        val endTime = System.currentTimeMillis() - startTime
        println("traditional cost-->$endTime")
    }
}

fun completableFutureApproach(images: List<String>, executor: java.util.concurrent.Executor) {
    // 1. 并行压缩所有图片
    val startTime = System.currentTimeMillis()
    val compressFutures = images.map { image ->
        CompletableFuture.supplyAsync({ compressImage(image) }, executor)
    }

    println("compressFutures")

    val uploadFutures = compressFutures.map { future ->
        future.thenComposeAsync({ compressedImage ->
            CompletableFuture.supplyAsync({ uploadImage(compressedImage) }, executor)
        }, executor)
    }
    println("uploadFutures")

    // 3. 等待所有上传完成
    val allUploads = CompletableFuture.allOf(*uploadFutures.toTypedArray())

    // 4. 最终发布（依赖上传完成）
    allUploads.thenRunAsync({
        submit("早安")
        println("发布成功")
        val endTime = System.currentTimeMillis() - startTime
        println("completableFutureApproach cost-->$endTime")
    }, executor)
    allUploads.join()
    println("所有流程完成")
}

suspend fun coroutines() = coroutineScope {
    val results = HashMap<String, String>()
    val deferredCompress = images.map {
        async {
            val result = compressImageAsync(it)
            results[it] = result
        }
    }
    deferredCompress.awaitAll()
    println("压缩完成")
    val deferredUpload = images.map {
        async {
            uploadImageAsync(results[it]!!)
        }
    }
    deferredUpload.awaitAll()
    println("上传完成")
    submitAsync("早安")
    println("发布成功")

}

fun compressImage(imagePath: String): String {
    Thread.sleep(100)
    return imagePath + "_compress"
}

suspend fun compressImageAsync(imagePath: String): String = suspendCoroutine {
    executor.execute {
        Thread.sleep(100)
        it.resume(imagePath + "_compress")
    }
}


fun uploadImage(file: String): String {
    Thread.sleep(400)
    return "url_$file"
}

suspend fun uploadImageAsync(file: String): String = suspendCoroutine {
    executor.execute {
        Thread.sleep(400)
        it.resume("url_$file")
    }
}

fun submit(string: String): String {
    Thread.sleep(200)
    return "200"
}

suspend fun submitAsync(string: String) = suspendCoroutine {
    executor.execute {
        Thread.sleep(200)
        it.resume("200")
    }
}