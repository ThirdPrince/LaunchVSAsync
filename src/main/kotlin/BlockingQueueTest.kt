import utils.log
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

fun main() {
    val blockingQueue = LinkedBlockingQueue<Int>(5)
    blockingQueue.offer(1)
    blockingQueue.offer(2)
    thread {
        while (true) {
            log("get-->"+blockingQueue.take())
        }

    }
    thread {
        repeat(10) { it->
            log("offer-->")
            blockingQueue.offer(it)
            Thread.sleep(1000)
        }
    }
    Thread.sleep(100000)
}