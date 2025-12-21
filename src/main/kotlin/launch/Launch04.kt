package launch

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import utils.log

val semaphore = Semaphore(3)
fun main() = runBlocking {
    val jobs = List(10) {
        launch(Dispatchers.IO) {
            semaphore.withPermit {
                // 临界区代码
                log("is in critical section")
                delay(2000*3)
            }
        }
    }
    jobs.forEach { it.join() }
}