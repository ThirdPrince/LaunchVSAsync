package javademo

import utils.ClientManager
import utils.log
import java.util.concurrent.CountDownLatch
import javax.imageio.ImageIO
import kotlin.system.measureTimeMillis

fun main() {
    val userList = listOf(1, 2, 3, 4, 5,6,7,8,9)
    val timeCost = measureTimeMillis {
        val countDownLatch = CountDownLatch(userList.size)
        userList.forEach {
            ClientManager.getUser(it) {
                countDownLatch.countDown()
            }
        }
        countDownLatch.await()
    }

    log("timeCost-->$timeCost")
}