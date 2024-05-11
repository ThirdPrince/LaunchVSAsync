package flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import utils.log

fun function1():String{
    return  "Function 1"
}
fun function2():String{
    return  "Function 2"
}

fun function3():String{
    return  "Function 3"
}
fun main() = runBlocking {
    val flow = flowOf(::function1,::function2,::function3)
   flow.onEach {
       delay(1000)
   }.collectIndexed { _, value ->
     log(value())

   }
    log("end")
}