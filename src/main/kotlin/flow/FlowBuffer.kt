package flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import utils.log


fun main() = runBlocking {
   flow {
       emit(1)
       delay(1000)
       emit(2)
       delay(1000)
       emit(3)
   }.onEach {
       log("$it is ready")
   }.buffer().collect{
       delay(1000)
       log("$it is handled")
   }

}