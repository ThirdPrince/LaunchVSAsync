package flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import utils.log


fun main() = runBlocking {
   flow {
       var count = 0
       while (true){
           emit(count)
           delay(1000)
           count++
       }
   }.conflate().collect{
       log("start handle-->$it")
       delay(2000)
       log("finish handler -->$it")
   }

}