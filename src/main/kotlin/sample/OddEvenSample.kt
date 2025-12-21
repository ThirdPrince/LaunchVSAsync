package sample

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.log

fun main() = runBlocking {
    val oddComplete = CompletableDeferred<Unit>()
    val evenComplete = CompletableDeferred<Unit>()
    var count = 0
    val job1 = launch {
        while (count % 2 != 0) {
            log("odd","count-->$count")
            count++
            delay(2000)
            evenComplete.complete(Unit)
            oddComplete.await()
        }
    }

    val job2 = launch {
        while (count % 2 == 0) {
            log("even","count-->$count")
            count++
            delay(2000)
            oddComplete.complete(Unit)
            evenComplete.await()
        }
    }
    job1.join()
    job2.join()

    log("end")

}