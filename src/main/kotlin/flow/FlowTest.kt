package flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.log

fun main() = runBlocking{
    val flow = flow<Pair<Int, String>> {
        emit(Pair(0, "200"))
        emit(Pair(1, "199"))
        emit(Pair(2, "198"))
        emit(Pair(3, "197"))
        delay(3000)

        emit(Pair(0, "200"))
        emit(Pair(1, "199"))
        emit(Pair(2, "198"))
        emit(Pair(3, "197"))
        emit(Pair(4, "196"))
        delay(3000)

        emit(Pair(1, "199"))
        emit(Pair(2, "198"))
        emit(Pair(3, "197"))
    }

    val currentWindow = mutableMapOf<Int, String>()
    var lastProcessTime = System.currentTimeMillis()

    launch {
        flow.collect { pair ->
            currentWindow[pair.first] = pair.second

            // 检查是否应该处理当前窗口（1秒间隔）
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastProcessTime >= 1000) {
                currentWindow.clear()
                currentWindow[pair.first] = pair.second
                lastProcessTime = currentTime
            }
        }
    }
    launch {
        delay(1000)
        log("current avatar$currentWindow")
        delay(3000)
        log("current avatar$currentWindow")
        delay(4000)
        log("current avatar$currentWindow")
    }
    log("end")

}





