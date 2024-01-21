package flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import utils.log


fun main() = runBlocking {
   val start = System.currentTimeMillis()
    val flow1 = flow<String> {
        delay(3000)
        emit("a")
    }
    val flow2 = flow<Int>{
        delay(2000)
        emit(1)
    }

    flow1.zip(flow2){
        a,b -> a +b
    }.collect{
        val end = System.currentTimeMillis()
        log("time cost -->${end - start} -->$it")
    }

}